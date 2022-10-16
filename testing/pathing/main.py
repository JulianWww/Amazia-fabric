from data import path
import numpy as np
import matplotlib.pyplot as plt

path = np.array(path)
plt.plot(path[:,0], path[:,1])
plt.show()