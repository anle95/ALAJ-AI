import matplotlib.pyplot as plt
import numpy as np

# french = [36961, 2503, 43621, 2992, 15694, 1042, 36231, 2487, 29945, 2014, 40588, 2805, 75255, 5062, 37709, 2643,
#           30899, 2126, 25486, 1784, 37497, 2641, 40398, 2766, 74105, 5047, 76725, 5312, 18317, 1215]
# english = [35680, 2217, 42514, 2761, 15162, 990, 35298, 2274, 29800, 1865, 40255, 2606, 74532, 4805, 37464, 2396,
#            31030, 1993, 24843, 1627, 36172, 2375, 39552, 2560, 72545, 4597, 75352, 4871, 18031, 1119]

french_L = []
english_L = []
french_A = []
english_A = []
# read file
file_data = open('libsvm_format.txt', 'r')
data_txt = file_data.read()
# parse file
data_list = data_txt.split('\n')
print(data_list)
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
q = int(q / 2)
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


# perceptron algorithm
def perceptron(data, limit):
    w = np.zeros(3)
    y = data[:, 0]
    x = data[:, 1:]
    H = lambda wx: 1 if wx >= 0 else 0
    iterations = 0
    miss = limit + 1
    while miss > limit:
        iterations += 1
        nums = np.random.permutation(len(data))
        for i in nums:
            wx = w[0] + w[1]*x[i, 0] + w[2]*x[i, 1]
            w += (y[i] - H(wx)) * np.array([1.0,
                                            x[i, 0],
                                            x[i, 1]])
        wx = w[0] + w[1]*x[:, 0] + w[2]*x[:, 1]
        y_hat = [H(wxs) for wxs in wx]
        miss = (abs(data[:, 0] - y_hat)).sum()
    return [w, iterations]

[w, iterations] = perceptron(np.array(data), 0)
print('Stochastic perceptron iterations:', iterations)
x = [0, 1]
y = [-w[0] / w[2] - w[1] / w[2] * x[0], -w[0] / w[2] - w[1] / w[2] * x[1]]
plt.plot(x, y, 'g')
plt.show()
print(w[0], w[1], w[2])
