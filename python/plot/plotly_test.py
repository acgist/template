import plotly.express as px

x_arr = [1, 2, 3, 4, 5]
y_arr = [v ** 2 for v in x_arr]
bar = px.bar(x=x_arr, y=y_arr)
bar.show()
