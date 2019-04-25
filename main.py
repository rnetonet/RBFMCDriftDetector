import matplotlib
from matplotlib import colors as mcolors

import matplotlib.pyplot as plt

from rbf import RBFDriftDetector

# Matplotlib
fig = plt.figure()
font = {"family": "monospace", "weight": "regular", "size": 6}

matplotlib.rc("font", **font)

# Dataset
moments = list(range(10))
values = [0.11, 0.12, 0.13, 0.34, 0.45, 0.47, 0.33, 0.25, 0.14, 0.10]

# Setup drift detector
drift_detector = RBFDriftDetector(sigma=0.2, threshold=0.6)

# Variables used in plotting
x = []
y = []
drifts_moments = []

subplots_x = 2
subplots_y = 5
subplot_id = 1

# blue, green, red, cyan, magent, yellow...
matplotlib_classic_colors = ["b", "g", "r", "c", "m", "y"]
map_center_color = {}

map_data_activated_center = {}

for moment, value in zip(moments, values):
    # Select subplot
    plt.subplot(subplots_x, subplots_y, subplot_id)
    subplot_id += 1

    # If drift detected, save to plot all drift lines in the current and next subplots
    drift_detector_response = drift_detector.handle(value)
    if drift_detector_response.concept_drift:
        drifts_moments.append(moment)

    # Draw drift lines
    for drift_moment in drifts_moments:
        plt.axvline(x=drift_moment, linestyle=":", color="salmon")

    # If the activated center has no color set
    # Setup one
    if drift_detector_response.activated_center not in map_center_color:
        map_center_color[
            drift_detector_response.activated_center
        ] = matplotlib_classic_colors.pop()

    # Save the activated_center for the point
    map_data_activated_center[
        (moment, value)
    ] = drift_detector_response.activated_center

    # Plot line
    x.append(moment)
    y.append(value)

    plt.plot(x, y, color="k", marker="o", linestyle="-")

    # Plot markers, with related center color
    # Why another loop ? I am overwriting to set the circle colors
    for _x, _y in zip(x, y):
        marker_color = map_center_color[map_data_activated_center[(_x, _y)]]
        plt.plot(_x, _y, color=marker_color, marker="o")

    # Labels
    plt.gca().set_title(
        f"instant:{subplot_id-1} | value:{value:.2f} | center:{drift_detector_response.activated_center}"
    )
    plt.gca().set_xticklabels([])

plt.show()
