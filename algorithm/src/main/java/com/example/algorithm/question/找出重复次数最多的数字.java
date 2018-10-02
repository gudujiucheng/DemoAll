package com.example.algorithm.question;
//   n+1个数字，数字范围（1-n）,一定存在重复数字,设计算法找出重复次数最多的数字，并说出算法时间、空间复杂度。
public class 找出重复次数最多的数字 {

    public static void main(String[] args) {
        test02();
    }



    //解题思路 1、首先数字范围是确定了1-n了，那么依据元素的值为下标，去找另外一个元素，并把这个元素+n，这样最后我们就可以判断元素值最大的那个就是重复次数最多的，但是这里取真正元素值的时候，要注意取余才是这能的元素值
    private static  void test03(){
        int[] aar = new int[]{0,10, 5, 8, 8,4,2, 3,2,2,3,0, 7, 1, 2, 3 };
        int n = aar.length-1;
        for (int i = 0; i <aar.length ; i++) {//aar的长度是n+1，数字范围是1-n
            //遍历数组，
            int x = aar[i]%n;//1、取真实元素值
            aar[x]+=n;//2、找到真实元素值对应索引位置的元素进行+n

        }

        int max = 0;
        int position = 0;
        for (int i = 0; i <aar.length ; i++) {
            if(aar[i]>max){
                max = aar[i];//找出最大值，然后最大数值对应的索引才是我们要的重复次数最多的那个数字的数值
                position = i;
            }
        }

        System.out.print("重复次数最多数字是："+position +"   重复了"+max/n+"次");

        //这个设计明显比桶排序更好一些，时间复杂度O(n),空间复杂度O(1)
    }


//   n+1个数字，数字范围（1-n）,一定存在重复数字,设计算法找出重复次数最多的数字，并说出算法时间、空间复杂度。

    private static void test02(){
        int[] aar = new int[]{0,10, 5, 8, 8,4,2, 3,2,2,3,0, 7, 1, 2, 3 };

        int n = aar.length -1;

        //创建一个新数组
        int[] temp  =  new int[n+1];
        // 第二步，向新建的数组里面填充数据（根据索引填充数据）
        for (int i = 0; i <aar.length ; i++) {
            int x = aar[i];
            temp[x]++;

        }
        //第四步 找出重复次数最多的，也就是找出数组的最大值【这里有个小问题，如果存在很多组重复次数是m次的，只是取出了一组】
        int tempValue = temp[0];
        int position =0;
        for (int i = 0; i <temp.length; i++) {
            if(temp[i]>tempValue){
                tempValue = temp[i];
                position = i;
            }

        }
        System.out.print("重复次数最多数字是："+position +"   重复了"+tempValue+"次");
        //很明显 空间复杂度 和 时间复杂度 都是O(n)

    }
}
