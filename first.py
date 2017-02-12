import matplotlib.pyplot as plt
import math
import numpy as np

french = [36961, 2503, 43621, 2992, 15694, 1042, 36231, 2487, 29945, 2014, 40588, 2805, 75255, 5062, 37709, 2643, 30899,
          2126, 25486, 1784, 37497, 2641, 40398, 2766, 74105, 5047, 76725, 5312, 18317, 1215]
english = [35680, 2217, 42514, 2761, 15162, 990, 35298, 2274, 29800, 1865, 40255, 2606, 74532, 4805, 37464, 2396, 31030,
           1993, 24843, 1627, 36172, 2375, 39552, 2560, 72545, 4597, 75352, 4871, 18031, 1119]

maxFreq = max(french + english)
french_L = np.array(french[0:len(french):2]) / maxFreq
english_L = np.array(english[0:len(english):2]) / maxFreq
french_A = np.array(french[1:len(french):2]) / maxFreq
english_A = np.array(english[1:len(english):2]) / maxFreq


#Batch gradient descent
def batch_gd(x, y, epsilon):
    alpha = 0.99
    w = np.zeros(2)
    iterations = 0
    grad = 1
    q = len(x)
    while abs(grad) > epsilon:
        iterations += 1
        gradsse = np.array([(y - (w[0] + w[1]*x)).sum(),
                            (x*(y - (w[0] + w[1]*x))).sum()])
        w += (alpha/q)*gradsse
        grad = 2*math.sqrt(gradsse[0]**2 + gradsse[1]**2)
    return [w, iterations]

# French
plt.plot(french_L, french_A, 'ro')
[w, iterations] = batch_gd(french_L, french_A, 0.001)
print('Batch GD (french) iterations: ', iterations)
x = [0, 1]
y = [w[0] + w[1]*x[0], w[0] + w[1]*x[1]]
plt.plot(x, y, 'r')

# English
plt.plot(english_L, english_A, 'bo')
[w, iterations] = batch_gd(english_L, english_A, 0.001)
print('Batch GD (english) iterations: ', iterations)
x = [0, 1]
y = [w[0]+w[1]*x[0], w[0]+w[1]*x[1]]
plt.plot(x, y, 'b')
plt.title('batch version')
plt.figure()


#Stochastic gradient descent
def stoch_gd(x, y, epsilon):
    alpha = 0.95
    w = np.zeros(2)
    iterations = 0
    grad = 1
    q = len(x)
    while grad > epsilon:
        iterations += 1
        nums = np.random.permutation(q)
        gradsse = np.zeros((q, 2))
        for i in nums:
            gradsse[i, :] = [y[i] - (w[0] + w[1]*x[i]),
                             x[i]*(y[i] - (w[0] + w[1]*x[i]))]
            w += alpha*gradsse[i]
        grad = 2*math.sqrt(gradsse[:, 0].sum() ** 2 + gradsse[:, 1].sum() ** 2)
    return [w, iterations]

# French
plt.plot(french_L, french_A, 'ro')
[w, iterations] = stoch_gd(french_L, french_A, 0.001)
print('Stochastic GD (french) iterations: ', iterations)
x = [0, 1]
y = [w[0] + w[1]*x[0], w[0] + w[1]*x[1]]
plt.plot(x, y, 'r')

# English
plt.plot(english_L, english_A, 'bo')
[w, iterations] = stoch_gd(english_L, english_A, 0.001)
print('Stochastic GD (english) iterations: ', iterations)
x = [0, 1]
y = [w[0] + w[1]*x[0], w[0] + w[1]*x[1]]
plt.plot(x, y, 'b')

plt.title('stochastic version')
plt.show()
