import matplotlib
import matplotlib.pyplot as plt
import pandas as pd
from matplotlib import colors as mcolors
from scipy.io import arff

from rbf import RBFDriftDetector
from scipy.io import arff
import pandas as pd

# Matplotlib
fig = plt.figure()
font = {"family": "monospace", "weight": "regular", "size": 7.5}

matplotlib.rc("font", **font)

# Dataset
data = arff.loadarff("stream.arff")
df = pd.DataFrame(data[0])

values = df["input"].to_list()
moments = range(len(values))

# moments = list(range(10))
# values = [0.11, 0.12, 0.13, 0.34, 0.45, 0.47, 0.33, 0.25, 0.14, 0.10]

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

    # Set limits
    plt.gca().set_xlim(left=-1, right=len(moments))
    plt.gca().set_ylim(top=1.00)

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
        f"T{subplot_id-1} | V={value:.2f} | C={drift_detector_response.activated_center} | A={drift_detector_response.activation:.2f}"
    )
    plt.gca().set_xticklabels([])
    subplot_id += 1

plt.show()
