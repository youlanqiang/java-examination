import java.util.Arrays;
import java.util.Scanner;


/**
 * author youlanqiang
 */
public class SortAndSearch {

    public static void main(String[] args) {
        System.out.println("输入8个整数，请用空格分开，之后使用回车键结束:");
        try(Scanner scanner = new Scanner(System.in)) {
            int[] nums = null;
            if (scanner.hasNext()) {
                String param = scanner.nextLine();
                String[] arr = param.split(" ");
                nums = new int[arr.length];
                for (int i = 0; i < arr.length; i++) {

                    nums[i] = Integer.parseInt(arr[i]);
                }
                nums = sort(nums);
                System.out.println("排序结果:"+ Arrays.toString(nums));
            }
            System.out.println("请输出你需要查询的值:");
            if(scanner.hasNext()){
                String param = scanner.nextLine();
                int value = Integer.parseInt(param);
                if(exist(nums, value)){
                    System.out.println("数组中存在这个值");
                }else{
                    System.err.println("数组中不存在这个值");
                }
            }
        } catch (Exception e) {
            System.err.println("抱歉程序出错已退出。请输入数字。");
            //e.printStackTrace();
        }

    }

    public static boolean exist(int[] array, int value){
        int low = 0;
        int height = array.length - 1;
        while(low <= height){
            int mid = (low + height) / 2;
            if(value == array[mid]){
                //在正中间
                return true;
            }else if(value < array[mid]){
                height = mid - 1;
            }else{
                low = mid + 1;
            }
        }
        return false;
    }


    //排序
    public static int[] sort(int[] sourceArray) throws Exception {
        int[] arr = Arrays.copyOf(sourceArray, sourceArray.length);

        // 总共要经过 N-1 轮比较
        for (int i = 0; i < arr.length - 1; i++) {
            int min = i;

            // 每轮需要比较的次数 N-i
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    // 记录目前能找到的最小值元素的下标
                    min = j;
                }
            }

            // 将找到的最小值和i位置所在的值进行交换
            if (i != min) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }

        }
        return arr;
    }
}