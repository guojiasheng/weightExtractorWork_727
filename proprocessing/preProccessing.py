#!/usr/bin/env python
# -*- coding: utf-8 -*-
# @Date    : 2016-07-27 16:10:18
# @Author  : name (mail@yours.com)
# @Link    : http://yours.com
# @Version : $Id$

import sys
import numpy as np
import math

def getmatrix(file):
	index = 0
	listV = []
	file = open(file)
	for line in file:
		if index:
			if index == 5:
				pass
			else:
				listV.append(line)
			index -= 1
		if "Query position weight matrix" in line:
			index = 5
	return listV

def matrixReverse(file):
	count = 0
	index = 0

	Matrix = []
	Result = []

	temp = getmatrix(file)

	for line in temp:
		value = line.strip().split("\t")
		
		if len(value) == 1:
			continue
		index += 1

		Matrix.append(value[1:])

		if index == 4 :
			count += 1
			#print "-" * 10
			index = 0
			A = np.transpose(Matrix)
			Result.append(A)
			Matrix = []
	return Result


		
def avg(originMatrixA,originMatrixB,resultFile):
	
	#fileMatrix = open("/Users/guojiasheng/Desktop/zou/resultM.txt","w")
	fileMatrix = open(resultFile,"w")

	A = matrixReverse(originMatrixA)
	count = 0
	for x in A:
		print count
		count +=1
		for item in x:
			item = map(float,item)
			m = float(sum(item))
			freList = []
			for indiv in item:
				fre = float(indiv)/m
				freList.append(str(fre))
			fileMatrix.write(",".join(freList)+"\n")
		fileMatrix.write("num:%d \n" %count)

	A = matrixReverse(originMatrixB)
	for x in A:
		print count
		count +=1
		for item in x:
			item = map(float,item)
			m = float(sum(item))
			freList = []
			for indiv in item:
				fre = float(indiv)/m
				freList.append(str(fre))
			fileMatrix.write(",".join(freList)+"\n")
		fileMatrix.write("num:%d \n" %count)

if __name__ == "__main__":
	if len(sys.argv) != 4:
		print "Usage: python preProccessing.py profileA profileB Maxtrix"
		exit()
	avg(sys.argv[1],sys.argv[2],sys.argv[3])
#avg()
#getmatrix()


