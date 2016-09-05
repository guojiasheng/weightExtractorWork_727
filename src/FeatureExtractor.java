import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeatureExtractor {

	
	//加载矩阵信息AGCT四行，204个矩阵，用 map 存储矩阵的列（AGCT对应的频率值） 
	//ArrayList 就是一个矩阵的的所有列，然后外层ArrayList再加一个ArrayList保存204个矩阵的信息
	public static ArrayList<ArrayList<Map<String, String>>> loadMaxtrix(String filePath){
		 File file = new File(filePath);
		 BufferedReader reader = null;
		 try {
	            System.out.println("以行为单位读取文件内容，一次读一整行：");
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            ArrayList<ArrayList<Map<String, String>>> Matrix = new ArrayList<ArrayList<Map<String, String>>>();
	            ArrayList<Map<String, String>> List = new ArrayList<Map<String, String>>();
	            while ((tempString = reader.readLine()) != null) {
	            	String value[] = tempString.split(",");
	            	if(tempString.startsWith("num")){
	            		 Matrix.add(List);
	            		 List = new ArrayList<Map<String, String>>();
	            		 continue;
	            	}
	            	Map<String, String> map = new HashMap<String, String>();
	            	map.put("A", value[0]);
	            	map.put("G", value[1]);
	            	map.put("C", value[2]);
	            	map.put("T", value[3]);
	            	List.add(map);
	            }
	            reader.close();
	            return Matrix;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } 
		return null;
	}
	
	//窗口匹配得分，获取最高值,一条序列的窗口有seq.length()-size+1 个
	public static Float  windowsScore (String seq,ArrayList<Map<String, String>> Matrix){
			int size = Matrix.size();   //矩阵长度
			float bestScore = 0;
			for (int j =0 ;j< seq.length()-size+1;j++){        //一条序列应该有length-size+1个阅读框
				
				float oneScore = 0;                            //一个阅读框的得分
				for (int i=j;i<j+size;i++){                    //遍历序列的每一个字符号
					String key = seq.substring(i,i+1);                        
					Map<String, String> value = Matrix.get(i-j);       //获取矩阵的对应列
					String score = value.get(key);                      //获取得分
					oneScore += Float.parseFloat(score);
				}
				if (bestScore < oneScore/size){              //取最高值
					bestScore = oneScore/size;
				}
			}
    	return bestScore;
	}
	
	//特征提取
	public static void featureScore(String arffPath,String label,ArrayList<ArrayList<Map<String, String>>> Matrix,BufferedWriter writer) throws IOException{
		 File file = new File(arffPath);
		 BufferedReader reader = null;
		 try {
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	        
	            while ((tempString = reader.readLine()) != null) {
	            	if(tempString.startsWith(">")){
	            		 continue;
	            	}
	            	for (int i=0;i<Matrix.size();i++){
	            		float score = windowsScore(tempString,Matrix.get(i));
	            		writer.write(score + ",");   	
	            	}
	            	writer.write(label + "\n");
	            		
	            }
	            reader.close();
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	}
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ArrayList<ArrayList<Map<String, String>>> Matrix = loadMaxtrix("./resultM.txt");
		if (Matrix == null){
			 System.out.println("矩阵加载有误");
			 return ;
		}
		
		String nFilePath = args[0];// 负例
		String pFilePath = args[1]; //正例
		String outPath = args[2]; //正例
		
		FileWriter fw = null;
	    BufferedWriter writer = null;
	    fw = new FileWriter(outPath);
        writer = new BufferedWriter(fw);
        
        writer.write("@Relation 727 \n");
        for(int i =0 ;i<Matrix.size();i++){
        	 writer.write("@ATTRIBUTE " + i +" NUMERIC\n");
        }
        writer.write("@ATTRIBUTE class {0,1} \n");
        writer.write("@data \n");
        
        featureScore(nFilePath,"0",Matrix,writer);  
        featureScore(pFilePath,"1",Matrix,writer);
		//featureScore("non-sigma54promoter-fasta-format.txt","0",Matrix,writer);
		//featureScore("sigma54promoter-fasta-format.txt","1",Matrix,writer);
		writer.close();
		
	}

}
