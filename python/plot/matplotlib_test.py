import matplotlib.pyplot as plt

arr = [1, 2, 3, 4, 5]
# plt.plot(arr)
# plt.show()
fig, ax = plt.subplots()
ax.plot(arr)
plt.show()
fig, ax = plt.subplots(2)
ax[0].plot(arr)
ax[1].plot(arr)
plt.show()
x_arr = [1, 2, 3, 4, 5]
y_arr = [v ** 2 for v in x_arr]
plt.scatter(x_arr, y_arr, c = range(5), cmap = plt.cm.Blues)
plt.show()

fig, ax = plt.subplots(figsize=(10, 10), dpi=100)
ax.scatter(x_arr, y_arr, c=range(5), cmap=plt.cm.Blues, edgecolors='none', s=15)
ax.set_aspect('equal')
ax.scatter(0, 0, c='green', edgecolors='none', s=100)
ax.scatter(x_arr[-1], y_arr[-1], c='red', edgecolors='none', s=100)
plt.show()
