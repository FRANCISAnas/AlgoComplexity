import random
import sys

# in case but... probably won't be used... but could be/have been -> especially "immutable"
pr = "PRESENT"
rm = "REMOVED"
im = "IMMUTABLE"

verbose = False
h_id = 0
if len(sys.argv) > 1:
    if sys.argv[1] == "-v":
        verbose = True
        if len(sys.argv) > 2:
            h_id = int(sys.argv[2])
    else:
        h_id = int(sys.argv[1])
        if len(sys.argv) > 2 and sys.argv[2] == "-v":
            verbose = True


def print_if_verbose(msg):
    if verbose:
        print(msg)


class Vertex:
    def __init__(self, red, status):
        self.red = red
        self.status = status
        self.outgoing = {}
        self.incoming = {}

    def rm_neighbor(self, id):
        self.outgoing.pop(id, None)
        col = self.incoming.pop(id, None)
        if col is not None:
            self.red = col

    def to_string(self, graph):
        neighbors = ""
        for key, edge_col in self.outgoing.items():
            neighbors += "  " + color(edge_col) + " --> " + str(key) + " " + "\n"
        for key, edge_col in self.incoming.items():
            neighbors += "  " + color(edge_col) + " <-- " + str(key) + " " + "\n"
        status = ""
        if self.status != pr:
            status = " - " + self.status
        return color(self.red) + status + "\n" + neighbors

    def print_state(self, graph):
        print_if_verbose(self.to_string(graph))

    # Outgoing edges
    def outgoing_red(self):
        res = []
        for key, edge_col in self.outgoing.items():
            if edge_col:
                res.append(key)
        return res

    def outgoing_blue(self):
        res = []
        for key, edge_col in self.outgoing.items():
            if not edge_col:
                res.append(key)
        return res

    def outgoing_blue_to_red(self, graph):
        res = []
        out = self.outgoing_blue()
        for i in out:
            if graph[i].red:
                res.append(i)
        return res

    def outgoing_red_to_blue(self, graph):
        res = []
        out = self.outgoing_red()
        for i in out:
            if not graph[i].red:
                res.append(i)
        return res

    def outgoing_blue_to_blue(self, graph):
        res = []
        out = self.outgoing_blue()
        for i in out:
            if not graph[i].red:
                res.append(i)
        return res

    def outgoing_red_to_red(self, graph):
        res = []
        out = self.outgoing_red()
        for i in out:
            if graph[i].red:
                res.append(i)
        return res

    # Incoming edges
    def incoming_red(self):
        res = []
        for key, edge_col in self.incoming.items():
            if edge_col:
                res.append(key)
        return res

    def incoming_blue(self):
        res = []
        for key, edge_col in self.incoming.items():
            if not edge_col:
                res.append(key)
        return res

    def diff_out_red_blue_and_blue_red(self, graph):
        return len(self.outgoing_red_to_blue(graph)) - len(self.outgoing_blue_to_red(graph))

    def diff_out_red_blue_and_blue_red_v2(self, graph):
        return len(self.outgoing_red_to_blue(graph)) * len(graph) / 2 + len(self.outgoing_blue_to_blue(graph)) - (
                len(self.outgoing_blue_to_red(graph)) * len(graph) / 2 + len(self.outgoing_red_to_red(graph)))

    def diff_out_red_blue_and_blue_red_v2_bis(self, graph):
        return len(self.outgoing_red_to_blue(graph)) + len(self.outgoing_blue_to_blue(graph)) - 3 * len(
            self.outgoing_blue_to_red(graph)) - len(self.outgoing_red_to_red(graph))


######## printing functions for debug ########
def color(red):
    if (red):
        return "RED"
    return "BLU"


def print_graph(graph):
    for key, v in graph.items():
        print_if_verbose("" + str(key) + ": " + v.to_string(graph))


######## treatments on vertices ######## not used -> will be removed... maybe
def immutable(graph, id):
    graph[id].status = im
    print_if_verbose("Set to immutable: " + str(id))


def remove(graph, id):
    rmd = graph.pop(id, None)  # not supposed to happen ('None') just in case
    rmd.status = rm

    for key, v in graph.items():
        v.rm_neighbor(id)
    print_if_verbose("Removed : " + str(id))
    return id


######## utility ########
# get red vertices
def get_red_indexes(graph):
    res = []
    for key, v in graph.items():
        if v.red:
            res.append(key)
    return res


######## heuristics ########
# random
def heuristic0(graph):
    red_indexes = get_red_indexes(graph)  # get red vertex indexes
    rand = random.randrange(len(red_indexes))  # random id
    return remove(graph, red_indexes[rand])  # remove "chosen" red vertex


# find red vertex with least outgoing blue edges to red vertices 
def heuristic1(graph):
    red_indexes = get_red_indexes(graph)  # get red vertex indexes
    rm_id = red_indexes[0]  # get first red index
    rm_out = graph[rm_id].outgoing_blue_to_red(graph)  # store useful edges
    red_indexes.remove(rm_id)  # rm_id reference
    for r in red_indexes:  # look for red vertex with least outgoing blue edges to red vertices
        out = graph[r].outgoing_blue_to_red(graph)
        if len(out) < len(rm_out):
            rm_id = r
            rm_out = out
    return remove(graph, rm_id)  # remove chosen red vertex


# find red vertex with least outgoing blue edges to red vertices AND (if exaequo) most outgoing blue edges to blue vertices
def heuristic2(graph):
    red_indexes = get_red_indexes(graph)  # get red vertex indexes
    rm_id = red_indexes[0]  # get first red index
    rm_out = graph[rm_id].outgoing_blue_to_red(graph)  # store useful edges
    red_indexes.remove(rm_id)  # rm_id reference
    exaequo_id = []
    for r in red_indexes:  # look for red vertex with least outgoing blue edges to red vertices
        out = graph[r].outgoing_blue_to_red(graph)
        if len(out) == len(rm_out):
            exaequo_id.append(r)
        else:
            if len(out) < len(rm_out):
                exaequo_id = []
                rm_id = r
                rm_out = out
    if len(exaequo_id) != 0:  # in the case of equality between vertices
        rm_out = graph[rm_id].outgoing_blue_to_blue(graph)  # store useful edges
        for r in exaequo_id:
            out = graph[r].outgoing_blue_to_blue(
                graph)  # look for selected vertex with most outgoing blue edges to blue vertices
            if len(out) > len(rm_out):
                rm_id = r
                rm_out = out
    return remove(graph, rm_id)


def difference(graph, vertex, version):
    if version == 3:
        return vertex.diff_out_red_blue_and_blue_red(graph)
    else:
        if version == 4:
            return vertex.diff_out_red_blue_and_blue_red_v2(graph)


# find red vertex with best difference! V1: red->blue >>> blue->red
def heuristic3_4_common(graph, version):
    red_indexes = get_red_indexes(graph)  # get red vertex indexes
    rm_id = red_indexes[0]  # get first red index
    rm_diff = difference(graph, graph[rm_id], version)  # store useful edges
    red_indexes.remove(rm_id)  # rm_id reference
    for r in red_indexes:  # look for red vertex with least outgoing blue edges to red vertices
        diff = difference(graph, graph[r], version)
        if diff > rm_diff:
            rm_id = r
            rm_diff = diff
    return remove(graph, rm_id)  # remove chosen red vertex


def heuristic3(graph):
    heuristic3_4_common(graph, 3)


# find red vertex with best difference! V2:
def heuristic4(graph):
    heuristic3_4_common(graph, 4)


######## simulation ########
# QUESTION 5 - build
def build_graph(n, p, q):
    graph = {}
    for i in range(n):
        red = 0
        if random.randrange(
                100) <= p * 100:  # probability for vertex to be red - random (0 - 100) with (p € [0, 1])*100
            red = 1
        graph[i] = Vertex(red, pr)  # red or blue vertex

    for key, v in graph.items():
        for i, nei in graph.items():
            if i != key:
                red = 1
                if random.randrange(100) <= q * 100:
                    red = 0
                graph[key].outgoing[i] = red
                graph[i].incoming[key] = red

    return graph


# condition
def one_red_left(graph):
    for key, v in graph.items():
        if v.red:
            return True
    return False


# run - apply same heuristic again and again until not possible anymore
def launch_simulation(graph, sequence, heuristic):
    print_if_verbose("INIT: " + str(len(graph)) + " vertices in graph \n")
    # print_graph(graph)
    while one_red_left(graph):
        sequence.append(heuristic(graph))  # append deleted vertex id to red sequence
    print_if_verbose("-----------------THE END-------------------\n")
    # print_graph(graph)
    # for key, v in graph.items():
    #    print("" + str(key) + ": " + v.to_string(graph))
    print_if_verbose("\nEnd: " + str(len(graph)) + " vertices left in graph\n")
    print_if_verbose("TOTAL: " + str(len(sequence)) + " vertices deleted")


# run all
def run_stats(times, n, heuristic):
    for p in range(11):
        for q in range(11):
            total = 0
            for i in range(times):
                sequence = []
                graph = build_graph(n, p / 10, q / 10)

                launch_simulation(graph, sequence, heuristic)
                # print(str(i) + ": " + str(len(sequence)) + " vertices deleted")
                total += len(sequence)
            print("f(" + str(p / 10) + ", " + str(q / 10) + ") = " + str(int(total / 100)))


######## examples of handmade graphs ########
def example_from_subject():
    graph = {
        0: Vertex(0, pr),
        1: Vertex(1, pr),
        2: Vertex(1, pr),
        3: Vertex(0, pr),
        4: Vertex(1, pr),
        5: Vertex(0, pr),
        6: Vertex(0, pr),
        7: Vertex(1, pr)
    }

    graph[0].red = 0
    graph[1].red = 1
    graph[2].red = 1
    graph[3].red = 0
    graph[4].red = 1
    graph[5].red = 0
    graph[6].red = 0
    graph[7].red = 1

    # OUTGOING
    graph[0].outgoing[1] = 0
    graph[0].outgoing[7] = 0

    graph[1].outgoing[0] = 1
    graph[1].outgoing[2] = 0
    graph[1].outgoing[6] = 0

    graph[2].outgoing[5] = 0
    graph[2].outgoing[6] = 1

    graph[3].outgoing[2] = 1
    graph[3].outgoing[4] = 1
    graph[3].outgoing[5] = 0

    graph[4].outgoing[5] = 1
    graph[4].outgoing[6] = 0

    graph[5].outgoing[2] = 0

    graph[7].outgoing[6] = 1

    # INCOMING
    graph[0].incoming[1] = 1

    graph[1].incoming[0] = 0

    graph[2].incoming[1] = 0
    graph[2].incoming[3] = 1
    graph[2].incoming[5] = 0

    graph[4].incoming[3] = 1

    graph[5].incoming[2] = 0
    graph[5].incoming[3] = 0
    graph[5].incoming[4] = 1

    graph[6].incoming[1] = 0
    graph[6].incoming[2] = 1
    graph[6].incoming[4] = 0
    graph[6].incoming[7] = 1

    graph[7].incoming[0] = 0

    return graph


######## main ########

# get graph

heuristics = {0: heuristic0, 1: heuristic1, 2: heuristic2, 3: heuristic3, 4: heuristic4}

heuristic_to_run = heuristic0

if h_id in heuristics:
    heuristic_to_run = heuristics[h_id]

run_stats(100, 100, heuristic_to_run)

# graph = example_from_subject()
'''
graph = build_graph(100, 0.5, 0.5)
red_sequence = []'''

# launch
# launch_simulation(graph, red_sequence, heuristic_to_run)
# print(str(len(red_sequence)) + " vertices deleted")

# launch_simulation(graph, heuristic0)
# launch_simulation(graph, heuristic1)
# launch_simulation(graph, heuristic2)
# launch_simulation(graph, heuristic3)
# launch_simulation(graph, heuristic4)
