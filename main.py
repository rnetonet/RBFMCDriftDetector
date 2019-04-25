import matplotlib
from matplotlib import colors as mcolors

import matplotlib.pyplot as plt

from rbf import RBFMCDriftDetector

# Matplotlib
fig = plt.figure()

# Dataset
moments = list(range(10))
values = [0.11, 0.12, 0.13, 0.34, 0.45, 0.47, 0.33, 0.25, 0.11, 0.13]

# Setup drift detector
drift_detector = RBFMCDriftDetector(sigma=0.2, threshold=0.6)

# Variables used in plotting
x = []
y = []
drifts_moments = []

subplots_x = 2
subplots_y = 5
subplot_id = 1

centers = {}

# Centers colors
centers_colors = list(mcolors.TABLEAU_COLORS.values())[1:]

for moment, value in zip(moments, values):
    x.append(moment)
    y.append(value)

    # Select subplot
    plt.subplot(subplots_x, subplots_y, subplot_id)
    subplot_id += 1

    # Labels of the subplot
    plt.gca().set_title(f"{subplot_id}")
    # plt.gca().set_yticklabels([])
    plt.gca().set_xticklabels([])

    # Plot line
    plt.plot(x, y, marker="o")

    # If drift detected, save to plot all drift lines in the current and next subplots
    if drift_detector.handle(value):
        drifts_moments.append(moment)

    # Draw drift lines
    for drift_moment in drifts_moments:
        plt.axvline(x=drift_moment, linestyle=":", color="salmon")

    # If center, save for ploting later
    if drift_detector.is_center(value) and value not in centers.values():
        centers[moment] = {"y": value, "color": centers_colors.pop()}

    # Plot the centers
    for moment, center_data in centers.items():
        plt.gca().plot((moment), (center_data["y"]), "o", color=center_data["color"])

plt.show()
