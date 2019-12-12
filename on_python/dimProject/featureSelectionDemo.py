import numpy as np
from sklearn import feature_selection
from sklearn import datasets

'''
当数据预处理完成后，我们需要选择有意义的特征输入机器学习的算法和模型进行训练。
通常来说，从两个方面考虑来选择特征：
1、特征是否发散：如果一个特征不发散，例如方差接近于0，也就是说样本在这个特征上基本上没有差异，这个特征对于样本的区分并没有什么用。
2、特征与目标的相关性：这点比较显见，与目标相关性高的特征，应当优选选择。除方差法外，本文介绍的其他方法均从相关性考虑。

根据特征选择的形式又可以将特征选择方法分为3种：
1、Filter：过滤法，按照发散性或者相关性对各个特征进行评分，设定阈值或者待选择阈值的个数，选择特征。
2、Wrapper：包装法，根据目标函数（通常是预测效果评分），每次选择若干特征，或者排除若干特征。
3、Embedded：嵌入法，先使用某些机器学习的算法和模型进行训练，得到各个特征的权值系数，根据系数从大到小选择特征。类似于Filter方法，但是是通过训练来确定特征的优劣。
'''


'''
类	                    所属方式              说明
VarianceThreshold	    Filter	             方差选择法(移除低方差的特征)
SelectKBest	            Filter	             可选关联系数、卡方校验、最大信息系数作为得分计算的方法
RFE	                    Wrapper	             递归地训练基模型，将权值系数较小的特征从特征集合中消除
SelectFromModel	        Embedded	         训练基模型，选择权值系数较高的特征


'''


iris = datasets.load_iris()

'''*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*= Filter =*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*'''

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 方差选择选择法 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
使用方差选择法，先要计算各个特征的方差，然后根据阈值，选择方差大于阈值的特征。
方差选择法，返回值为特征选择后的数据
参数threshold为方差的阈值
'''
featureVar = feature_selection.VarianceThreshold(threshold=0.5).fit_transform(iris.data)


'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 相关系数法 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
使用相关系数法，先要计算各个特征对目标值的相关系数以及相关系数的P值
选择K个最好的特征，返回选择特征后的数据
第一个参数为计算评估特征是否好的函数，该函数输入特征矩阵和目标向量，输出二元组（评分，P值）的数组，数组第i项为第i个特征的评分和P值。在此定义为计算相关系数
参数k为选择的特征个数
'''
from scipy import stats # 统计库

# map(function, iterable)，map会对iterable中的每个元素进行function操作
# 在使用map是，若报错：TypeError: float() argument must be a string or a number, not 'map'
# 不管用map这个函数把数据转变成了什么格式，不管是int还是float还是其它的，最后，都要把map完的数据转换成list，如果你要是需要array的话，可以再转换成array，但是要套在list外面
# 所以map往往和list一起使用：list(map(func,iterable))
score_func1 = lambda X,Y: (np.array(list(map(lambda x: stats.pearsonr(x,Y),X.T))).T[0],np.array(list(map(lambda x: stats.pearsonr(x,Y),X.T))).T[1])
# (array([ 0.78256123, -0.4194462 ,  0.94904254,  0.95646382]),
#  array([2.89047835e-32, 9.15998497e-08, 4.15547758e-76, 4.77500237e-81]))

# 这个输入不符合SelectKBest，会报错。
score_func2 = lambda X,Y: np.vsplit(np.array(list(map(lambda x: stats.pearsonr(x,Y),X.T))).T,2)
# [array([[ 0.78256123, -0.4194462 ,  0.94904254,  0.95646382]]),
#  array([[2.89047835e-32, 9.15998497e-08, 4.15547758e-76, 4.77500237e-81]])]

score_func3 = lambda X,Y: list(map(lambda x:np.array(x),np.array(list(map(lambda x: stats.pearsonr(x,Y),X.T))).T))
# (array([ 0.78256123, -0.4194462 ,  0.94904254,  0.95646382]),
#  array([2.89047835e-32, 9.15998497e-08, 4.15547758e-76, 4.77500237e-81]))

featurePearsonr = feature_selection.SelectKBest(score_func3, k=2).fit_transform(iris.data, iris.target)
# SelectKBest(score_func=f_classif, k=10) :通过相关系数来筛选特征，通过score_func，筛选得到相关度最高的k个特征
# score_func 的输入为X、Y，输出为np.array(F),np.array(P)的可迭代数组(元组、列表等)，即输出为相关系数的数组和p系数的数组
# 以score_func3为例
# 其中X为fit_transform()中的iris.data，Y为iris.targetX
# map(function, iterable)，map会对iterable中的每个元素进行function操作
# 本例中，iterable为X.T，即iris.data.T，即function中得到的每个元素为iris.data的一列
# stats.pearsonr(x,y): 计算特征与目标变量之间的相关度
# map外一定要嵌套list，不然会报错，list(map(func,iterable))

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 卡方检验 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
经典的卡方检验是检验定性自变量对定性因变量的相关性。假设自变量有N种取值，因变量有M种取值，考虑自变量等于i且因变量等于j的样本频数的观察值与期望的差距，构建统计量
这个统计量的含义简而言之就是自变量对因变量的相关性
feature_selection.chi2相关系计算函数
'''
featureChi2 = feature_selection.SelectKBest(feature_selection.chi2,k=2).fit_transform(iris.data,iris.target)

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 互信息法 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
互信息:评价定性自变量对定性因变量的相关性
'''

# minepy:minepy 提供 ANSI C 库的基于最大信息的非参数估计的实现
import minepy
# 由于MINE的设计不是函数式的，定义mic方法将其为函数式的，返回一个二元组，二元组的第2项设置成固定的P值0.5
def mic (x,y):
    m = minepy.MINE()
    m.compute_score(x,y)  # 计算x、y之间的最大标准互信息评分
    return (m.mic(),0.5)  # m.mic 返回最大信息系数

# 这次不用lambda了，这样更代码方便阅读
def score_func4(X,Y):
    micList,pList = [],[]
    for x in X.T:
        micValue,pValue= mic(x,Y)
        micList.append(micValue)
        pList.append(pValue)
    return np.array(micList),np.array(pList)

featureMic = feature_selection.SelectKBest(score_func4,k=2).fit_transform(iris.data,iris.target)



'''*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*= Wrapper =*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*'''
'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 递归特征消除法 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
递归消除特征法使用一个基模型来进行多轮训练，每轮训练后，移除若干权值系数的特征，再基于新的特征集进行下一轮训练。
RFE:
对特征含有权重的预测模型(例如，线性模型对应参数coefficients)，RFE通过递归减少考察的特征集规模来选择特征。
首先，预测模型在原始特征上训练，每个特征指定一个权重。之后，那些拥有最小绝对值权重的特征被踢出特征集。
如此往复递归，直至剩余的特征数量达到所需的特征数量。

RFECV:通过交叉验证的方式执行RFE，以此来选择最佳数量的特征：对于一个数量为d的feature的集合，他的所有的子集的个数是2的d次方减1(包含空集)。
指定一个外部的学习算法，比如SVM之类的。通过该算法计算所有子集的validation error。选择error最小的那个子集作为所挑选的特征。

'''
# 使用feature_selection库的RFE类来选择特征的代码如下

from sklearn import linear_model
featureREF = feature_selection.RFE(estimator=linear_model.LogisticRegression(),n_features_to_select=2).fit_transform(iris.data,iris.target)


'''*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*= Embedded =*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*'''
# 使用SelectFromModel选择特征
'''
单变量特征选择方法独立的衡量每个特征与响应变量之间的关系，另一种主流的特征选择方法是基于机器学习模型的方法。
有些机器学习方法本身就具有对特征进行打分的机制，或者很容易将其运用到特征选择任务中，例如回归模型，SVM，决策树，随机森林等等。'''
'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 基于惩罚项(L1、L2)的特征选择法 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
使用带惩罚项的基模型，除了筛选出特征外，同时也进行了降维。使用feature_selection库的SelectFromModel类结合带L1惩罚项的逻辑回归模型
'''
featureFromL1 = feature_selection.SelectFromModel(linear_model.LogisticRegression(penalty="l1",C=0.1)).fit_transform(iris.data,iris.target)
# L1惩罚项降维的原理在于保留多个对目标值具有同等相关性的特征中的一个，所以没选到的特征不代表不重要。故，可结合L2惩罚项来优化。
# 具体操作为：若一个特征在L1中的权值为1，选择在L2中权值差别不大且在L1中权值为0的特征构成同类集合，将这一集合中的特征平分L1中的权值，故需要构建一个新的逻辑回归模型

'''
使用feature_selection库的SelectFromModel类结合带L1以及L2惩罚项的逻辑回归模型，来选择特征的代码如下
'''
featureFromL1L2 = feature_selection.SelectFromModel(linear_model.LogisticRegression(C=0.1),threshold="0.8*mean").fit_transform(iris.data,iris.target)
# 对于SVM和逻辑回归，参数C控制稀疏性：C越小，被选中的特征越少。对于Lasso，参数alpha越大，被选中的特征越少。

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 随机稀疏模型 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
基于L1的稀疏模型的局限在于，当面对一组互相关的特征时，它们只会选择其中一项特征。
为了减轻该问题的影响可以使用随机化技术，通过多次重新估计稀疏模型来扰乱设计矩阵，或通过多次下采样数据来统计一个给定的回归量被选中的次数_。
RandomizedLasso 实现了使用这项策略的Lasso，RandomizedLogisticRegression 使用逻辑回归，适用于分类任务。要得到整个迭代过程的稳定分数，你可以使用 lasso_stability_path。
注意到对于非零特征的检测，要使随机稀疏模型比标准F统计量更有效，那么模型的参考标准需要是稀疏的，换句话说，非零特征应当只占一小部分。
'''

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 基于树的特征选择 -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
基于树的预测模型（见 sklearn.tree 模块，森林见 sklearn.ensemble 模块）能够用来计算特征的重要程度，因此能用来去除不相关的特征
'''
from sklearn.ensemble import ExtraTreesClassifier

X = iris.data
y = iris.target
print(X.shape)

clf = ExtraTreesClassifier()
clf.fit(X,y)
print(clf.feature_importances_)

featureTree = feature_selection.SelectFromModel(clf,prefit=True)

X_new = featureTree.transform(X)
print(X_new.shape)

'''
*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- 将特征选择过程融入pipeline -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
特征选择常常被当作学习之前的一项预处理。在scikit-learn中推荐使用sklearn.pipeline.Pipeline
'''
from  sklearn.pipeline import Pipeline
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import LinearSVC

clf = Pipeline([
  ('feature_selection', feature_selection.SelectFromModel(LinearSVC(penalty="l2"))),
  ('classification', RandomForestClassifier())
])

clf.fit(X, y)

# 在此代码片段中，将sklearn.svm.LinearSVC 和 sklearn.feature_selection.SelectFromModel 结合来评估特征的重要性，并选择最相关的特征。
# 之后 sklearn.ensemble.RandomForestClassifier 模型使用转换后的输出训练，即只使用被选出的相关特征。你可以选择其它特征选择方法，或是其它提供特征重要性评估的分类器