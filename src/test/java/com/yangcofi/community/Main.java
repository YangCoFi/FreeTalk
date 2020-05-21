package com.yangcofi.community;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName Main
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/24 14:26
 **/
public class Main {
    public ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        if(k < 1 || k > input.length){
            return new ArrayList<Integer>();
        }
        int[] heap = new int[k];
        for(int i = 0; i != k; i ++){
            heap[i] = input[i];
            heapInsert(heap, i);
        }
        for(int i = k; i < input.length; i ++){
            if(input[i] < heap[0]){
                heap[0] = input[i];
                heapify(heap, 0, k);
            }
        }
        List<Integer> list1 = Arrays.stream(heap).boxed().collect(Collectors.toList());
        return (ArrayList<Integer>) list1;
    }

    public static void main(String[] args) {
        int[] input = {4,5,1,6,2,7,3,8};
        System.out.println(new Main().GetLeastNumbers_Solution(input, 4));
    }

    public void heapInsert(int[] heap, int index){
        while(heap[index] > heap[(index - 1) / 2]){
            swap(heap, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public void heapify(int[] arr, int index, int size){
        int left = index * 2 + 1;
        while(left < size){
            int largest = left + 1 < size && arr[left + 1] > arr[left] ? left + 1 : left;
            largest = arr[largest] > arr[index] ? largest : index;
            if(largest == index){
                break;
            }
            swap(arr, largest, index);
            index = largest;
            left = index * 2 + 1;
        }
    }

    public void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
