# weightExtractorWork_727
提取特征

要求：

1. 编程读取一下profile文件，依次读取正例和反例的每一个motif的Query position weight matrix，最后那行IC不用。只有AGCT四行，这个矩阵是int（个数），你要转化成double（频率），比如第一列是0060，转化完就是0 0 1.0 0. 也就是每一位在本列所占的比重/频率.
 
正例和反例一共是99+105=204个矩阵。
 
2. 对附件的fasta文件。每个序列分别去扫每个矩阵，找到得分最大值，这样每个序列就转化成一个204维的向量了。
扫的方法如下：
假设矩阵A有10列，那就用长为10的阅读框扫这个序列s，计算该阅读框和矩阵比对得分。比如这一位是G，矩阵这一列是(0.1,0.2,0.3,0.4),那这一列的得分就是0.2. 把10位的得分都加起来，总分再除以10（长度）。就是这个阅读框的得分。一条序列应该有lenth-9个阅读框，取得分最高的分值，当做该序列在矩阵A的得分。
这样一个序列就有204个得分，每个序列就变成204维向量了（相当于提取特征）
 
3. 最后：1.arff文件；2.eclipse下的工程文件，代码后面我还要重用的，所以写的尽量规范。 （你写代码时最好考虑一下，如何把这204个矩阵也写到代码里，这样就可以变成一个特征提取的代码了，以后来新序列，直接就能提取特征，方便日后web server的搭建）

usage：

1) 提取204个矩阵到文件resultM.txt（这边必须为resultM.txt这个名字，因为为了参数少点，在java里面这个矩阵的名字我写死了）
  
  python preProccessing.py profileA profileB Maxtrix
    
  例如 python preProccessing.py 正例profile.txt 反例profile.txt resultM.txt


2) java工程文件里面  
   main 函数会生成arrf文件，
   
    featureScore(nFilePath,"0",Matrix,writer);  
    featureScore(pFilePath,"1",Matrix,writer);

    featureScore(file,lable,矩阵,writer);
   
   具体使用：
   java -jar 负例样本路径 正例样本路径 输出feature路径   
   java -jar extractor.jar non-sigma54promoter-fasta-format.txt sigma54promoter-fasta-format.txt feature.txt

