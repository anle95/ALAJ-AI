import matplotlib.pyplot as plt
import numpy as np
import math

# french = [36961, 2503, 43621, 2992, 15694, 1042, 36231, 2487, 29945, 2014, 40588, 2805, 75255, 5062, 37709, 2643, 30899,
#           2126, 25486, 1784, 37497, 2641, 40398, 2766, 74105, 5047, 76725, 5312, 18317, 1215]
# english = [35680, 2217, 42514, 2761, 15162, 990, 35298, 2274, 29800, 1865, 40255, 2606, 74532, 4805, 37464, 2396, 31030,
#            1993, 24843, 1627, 36172, 2375, 39552, 2560, 72545, 4597, 75352, 4871, 18031, 1119]

french_L = []
english_L = []
french_A = []
english_A = []
# read file
file_data = open('libsvm_format.txt', 'r')
data_txt = file_data.read()
# parse file
data_list = data_txt.split('\n')
q = len(data_list)
for r in range(q):
    row_list = data_list[r].split(' ')
    feats = [float(int(row_list[0]))]
    nrOfFeats = len(row_list)
    for i in range(1, nrOfFeats):
        feature_list = row_list[i].split(':')
        feats.append(float(feature_list[1]))
    if feats[0] == 0:
        french_L.append(feats[1])
        french_A.append(feats[2])
    elif feats[0] == 1:
        english_L.append(feats[1])
        english_A.append(feats[2])
data = []
q = int(q/2)
for i in range(q):
    data.append([0, french_L[i], french_A[i]])
    data.append([1, english_L[i], english_A[i]])
plt.plot(french_L, french_A, 'ro')
plt.plot(english_L, english_A, 'bo')

# write file
textFile = ''
for i in range(q-1):
    textFile += '0 ' + '1:' + str(french_L[i]) + ' 2:' + str(french_A[i]) + '\n'
    textFile += '1 ' + '1:' + str(english_L[i]) + ' 2:' + str(english_A[i]) + '\n'
textFile += '0 ' + '1:' + str(french_L[q-1]) + ' 2:' + str(french_A[q-1]) + '\n'
textFile += '1 ' + '1:' + str(english_L[q-1]) + ' 2:' + str(english_A[q-1])
file = open('libsvm_format.txt', 'w')
file.write(textFile)
file.close()
plt.plot(french_L, french_A, 'r.')
plt.plot(english_L, english_A, 'b.')
q *= 2

# logistic regression algorithm
alpha = 0.9
epsilon = 0.01
w = [0, 0, 0]
# w = [-0.684, 265, -275]
iterations = 0
grad = epsilon + 1
while grad > epsilon:
    # alpha = 1 / (1 + iterations / 1000)
    nums = np.random.permutation(q)
    iterations += 1
    term0 = []
    term1 = []
    term2 = []
    for j in nums:
        last = len(term0)
        wx = w[0] + w[1] * data[j][1] + w[2] * data[j][2]
        y_hat = 1 / (1 + math.pow(math.e, -wx))
        term0.append(data[j][0] - y_hat)
        term1.append(data[j][1] * term0[last])
        term2.append(data[j][2] * term0[last])
        w[0] += alpha * term0[last]
        w[1] += alpha * term1[last]
        w[2] += alpha * term2[last]
    grad0 = sum(term0)
    grad1 = sum(term1)
    grad2 = sum(term2)
    grad = math.sqrt(grad0**2 + grad1**2 + grad2**2)
print('grad:', grad)
print('Stochastic logistic regression iterations:', iterations)
x = [0, 1]
y = [-w[0]/w[2]-w[1]/w[2]*x[0], -w[0]/w[2]-w[1]/w[2]*x[1]]
plt.plot(x, y, 'g')
plt.show()
print(w[0], w[1], w[2])
