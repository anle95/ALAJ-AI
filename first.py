import matplotlib.pyplot as plt
import math
import numpy as np

french = [36961, 2503, 43621, 2992, 15694, 1042, 36231, 2487, 29945, 2014, 40588, 2805, 75255, 5062, 37709, 2643, 30899,
          2126, 25486, 1784, 37497, 2641, 40398, 2766, 74105, 5047, 76725, 5312, 18317, 1215]
english = [35680, 2217, 42514, 2761, 15162, 990, 35298, 2274, 29800, 1865, 40255, 2606, 74532, 4805, 37464, 2396, 31030,
           1993, 24843, 1627, 36172, 2375, 39552, 2560, 72545, 4597, 75352, 4871, 18031, 1119]
q = len(french)
french_A = []
english_A = []
french_L = []
english_L = []
for i in range(0, q, 2):
    french_L.append(french[i])
    french_A.append(french[i+1])
    english_L.append(english[i])
    english_A.append(english[i+1])
maxFreq = max(french_L + english_L + french_L + english_A)
q = int(q/2)
for i in range(q):
    french_L[i] /= maxFreq
    french_A[i] /= maxFreq
    english_L[i] /= maxFreq
    english_A[i] /= maxFreq
plt.plot(french_L, french_A, 'ro')
plt.plot(english_L, english_A, 'bo')

# GD - french
alpha = 0.99
epsilon = 0.001
w = [0, 0]
iterations = 0
grad = 1
while grad > epsilon:
    iterations += 1
    term0 = [];
    term1 = [];
    for j in range(q):
        term0.append(french_A[j]-(w[0]+w[1]*french_L[j]))
        term1.append(french_L[j] * (french_A[j] - (w[0] + w[1] * french_L[j])))
    sum0 = sum(term0)
    sum1 = sum(term1)
    grad0 = -2*sum0
    grad1 = -2*sum1
    w[0] += (alpha/q)*sum0
    w[1] += (alpha/q)*sum1
    grad = math.sqrt(grad0**2 + grad1**2)
print('Batch GD (french) iterations:', iterations)
x = [0, 1]
y = [w[0]+w[1]*x[0], w[0]+w[1]*x[1]]
plt.plot(x, y, 'r')

# GD - english
alpha = 0.99
epsilon = 0.001
w = [0, 0]
iterations = 0
grad = 1
while grad > epsilon:
    iterations += 1
    term0 = [];
    term1 = [];
    for j in range(q):
        term0.append(english_A[j]-(w[0]+w[1]*english_L[j]))
        term1.append(english_L[j] * (english_A[j] - (w[0] + w[1] * english_L[j])))
    sum0 = sum(term0)
    sum1 = sum(term1)
    grad0 = -2*sum0
    grad1 = -2*sum1
    w[0] += (alpha/q)*sum0
    w[1] += (alpha/q)*sum1
    grad = math.sqrt(grad0**2 + grad1**2)
print('Batch GD (english) iterations:', iterations)
x = [0, 1]
y = [w[0]+w[1]*x[0], w[0]+w[1]*x[1]]
plt.plot(x, y, 'b')
plt.title('batch version')
# plt.show()

# SGD - french
plt.plot(french_L, french_A, 'ro')
alpha = 0.95
epsilon = 0.001
w = [0, 0]
iterations = 0
grad = 1
while grad > epsilon:
    nums = np.random.permutation(q)
    iterations += 1
    term0 = []
    term1 = []
    sum0 = 0
    sum1 = 0
    for j in nums:
        term0 = (french_A[j]-(w[0]+w[1]*french_L[j]))
        term1 = (french_L[j] * (french_A[j] - (w[0] + w[1] * french_L[j])))
        w[0] += alpha * term0
        w[1] += alpha * term1
        sum0 += term0
        sum1 += term1
    grad0 = -2*sum0
    grad1 = -2*sum1
    grad = math.sqrt(grad0**2 + grad1**2)
print('Stochastic GD (french) iterations:', iterations)
x = [0, 1]
y = [w[0]+w[1]*x[0], w[0]+w[1]*x[1]]
plt.plot(x, y, 'r')

# SGD - english
plt.plot(english_L, english_A, 'bo')
alpha = 0.99
epsilon = 0.001
w = [0, 0]
iterations = 0
grad = 1
while grad > epsilon:
    nums = np.random.permutation(q)
    iterations += 1
    term0 = []
    term1 = []
    sum0 = 0
    sum1 = 0
    for j in nums:
        term0 = (english_A[j]-(w[0]+w[1]*english_L[j]))
        term1 = (english_L[j] * (english_A[j] - (w[0] + w[1] * english_L[j])))
        w[0] += alpha * term0
        w[1] += alpha * term1
        sum0 += term0
        sum1 += term1
    grad0 = -2*sum0
    grad1 = -2*sum1
    grad = math.sqrt(grad0**2 + grad1**2)
print('Stochastic GD (english) iterations:', iterations)
x = [0, 1]
y = [w[0]+w[1]*x[0], w[0]+w[1]*x[1]]
plt.plot(x, y, 'b')
plt.title('stochastic version')
plt.show()
