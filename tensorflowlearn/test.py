#
# import input_data
# import sys
# sys.path.append("/root/IdeaProjects/mian-component/tensorflowlearn")
#
# mnist = input_data.read_data_sets("/root/IdeaProjects/mian-component/tensorflowlearn/MNIST_data", one_hot=True)
#
import tensorflow as tf

m1 = tf.constant([3,5])
m2 = tf.constant([2,4])

result = tf.add(m1,m2)
print(result)