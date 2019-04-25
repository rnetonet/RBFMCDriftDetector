from simple_markov import MarkovChain

# initial facing of the coin
initial_probs = {"Heads": 0.2, "Tails": 0.5, "Other": 0.3}

# transition table for coin tosses
transition_table = {
    "Heads": [("Heads", 0.5), ("Tails", 0.2), ("Other", 0.3)],
    "Tails": [("Heads", 0.2), ("Tails", 0.2), ("Other", 0.6)],
    "Other": [("Heads", 0.3), ("Tails", 0.5), ("Other", 0.2)],
}

chain = MarkovChain(initial_probs, transition_table)

# get the first 10 coin tosses:
for i, step in zip(chain, range(10)):
    print("Step %d: %s" % (step + 1, i))

# View
from simple_markov.drawing.visualize import Visualizer

v = Visualizer()
print(v.draw_networkx(chain))
