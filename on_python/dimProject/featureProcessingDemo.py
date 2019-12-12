from sklearn.datasets import load_iris
from sklearn import preprocessing
import numpy as np


# 载入iris数据（鸢尾花样本数据）
iris = load_iris()
# iris.data : 样本数据中的特征数据
# iris.target : 样本数据中的样本目标向量


'''
    类	                功能                           说明
S@tandardScaler	      无量纲化              标准化，基于特征矩阵的列，将特征值转换至服从标准正态分布
MinMaxScaler	      无量纲化              区间缩放，基于最大最小值，将特征值转换到[0, 1]区间上
Normalizer	          归一化                基于特征矩阵的行，将样本向量转换为“单位向量”
Binarizer	          二值化                基于给定阈值，将定量特征按阈值划分
OneHotEncoder	      哑编码                将定性数据编码为定量数据
Imputer	              缺失值计算             计算缺失值，缺失值可填充为均值等
PolynomialFeatures	  多项式数据转换         多项式数据转换
FunctionTransformer   自定义单元数据转换      使用单变元的函数来转换数据
'''

## 无量纲化
# 无量纲化使不同规格的数据转换到同一规格
# 特征处理的过程都是针对列的(简单的想就是一列数据表示的是不同样本的同一个特征维度的值)
# 标准化，x'=(x-_x)/S，其中_x表示均值，S表示标准差
# 标准化的前提是特征值服从正态分布，标准化后，其转换成标准正态分布。
standData = preprocessing.StandardScaler().fit_transform(iris.data)  # 注意StandardScaler()的括号不能漏，有括号代表是实例化后的对象，没有括号代表的是一种方法

# 区间缩放，x'=(x-min)/(max-min)
# 区间缩放法利用了边界值信息，将特征的取值区间缩放到某个特点的范围，例如[0, 1]等
minMaxData = preprocessing.MinMaxScaler().fit_transform(iris.data)


## 归一化 : 使行的平方和为1，即使每个样本为单位向量
# 归一化和标准化的区别：归一化依照行处理，标准化依照列处理
# 归一化的应用场景：计算相似性
norData = preprocessing.Normalizer().fit_transform(iris.data)


## 定量特征二值化：大于阈值的为1，小于阈值的为0
binaryData = preprocessing.Binarizer(threshold=3).fit_transform(iris.data)


## 哑编码：onehot
onehotData = preprocessing.OneHotEncoder().fit_transform(iris.target.reshape((-1,1)))
# reshape(元组)，元组中用-1时，会用另外的维度计算出-1对应的shape大小。
# 本例中，iris.target.shape为(150,)，reshape((-1,1))，-1对应的大小为150/1=150，将即为reshape((150,1))

# 又发现一个问题：shape (150,)和(150,1)是不同的，(150,)表示1个数组，数组中有150个元素；(150,1)表示150维数组，每行1个向量
# x = np.array([1, 2])
# y = np.array([[1],[2]])
# z = np.array([[1,2]])
# print(x.shape)
# >>> (2,)
# print(y.shape)
# >>> (2, 1)
# print(z.shape)
# >>> (1, 2)
# x:[1,2]的shape值(2,)，意思是一维数组，数组中有2个元素
# y:[[1],[2]]的shape值是(2,1)，意思是一个二维数组，每行有1个元素
# z:[[1,2]]的shape值是（1，2），意思是一个二维数组，每行有2个元素


## 缺失值计算
# 缺失值计算，返回值为计算缺失值后的数据
# 参数missing_value为缺失值的表示形式，默认为NaN
# 参数strategy为缺失值填充方式，默认为mean（均值）
imputData = preprocessing.Imputer(missing_values="NaN", strategy="mean").fit_transform(np.vstack((np.array([np.nan]*4),iris.data)))


## 数据变换
# 多项式转换:默认为度数为2的多项式转换
#4个特征，度数为2的多项式转换：(x1,x2,x3,x4) -> (1,x1,x2,x3,x4,x1x1,x1x2,x1x3,x1x4,x2x2,x2x3,x2x4,x3x3,x3x4,x4x4)
polyData = preprocessing.PolynomialFeatures(degree=3).fit_transform(iris.data)

#基于单变元函数的数据转换（每个数据按照置顶函数转换，比如log1p()）
log1pData = preprocessing.FunctionTransformer(np.log1p).fit_transform(iris.data)
powerData = preprocessing.FunctionTransformer(np.square()).fit_transform(iris.data)