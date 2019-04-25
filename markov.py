class MarkovChain:
    initial_probs = {}
    transition_table = {}

    # transition_table = {
    #     "Heads": [("Heads", 0.5), ("Tails", 0.2), ("Other", 0.3)],
    #     "Tails": [("Heads", 0.2), ("Tails", 0.2), ("Other", 0.6)],
    #     "Other": [("Heads", 0.3), ("Tails", 0.5), ("Other", 0.2)],
    # }

    def transition(self, a, b):
        pass