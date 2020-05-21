package com.yangcofi.community;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName Solution
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/26 20:22
 **/
public class Solution {
    Queue<Integer> maxHeap = new PriorityQueue<>((o1, o2) -> o2 - o1);

    Queue<Integer> minHeap = new PriorityQueue<>();

    public void Insert(Integer num) {
        if(maxHeap.isEmpty()){
            maxHeap.offer(num);
            return;
        }
        if(maxHeap.peek() >= num){
            maxHeap.offer(num);
        }else{
            if(minHeap.isEmpty()){
                minHeap.offer(num);
                return;
            }
            if(minHeap.peek() > num){
                maxHeap.offer(num);
            }else{
                minHeap.offer(num);
            }
        }
        modifyTwoHeapSize();
    }

    public Double GetMedian() {
        int maxHeapSize = maxHeap.size();
        int minHeapSize = minHeap.size();
        if(maxHeapSize + minHeapSize == 0){
            return null;
        }
        int maxHeapHead = maxHeap.peek() == null ? 0 : maxHeap.peek();
        int minHeapHead = minHeap.peek() == null ? 0 : minHeap.peek();
        if(((maxHeapSize + minHeapSize) & 1) == 0){
            return (((double) minHeapHead + (double) maxHeapHead) / 2);
        }else if(maxHeapSize > minHeapSize){
            return (double)maxHeapHead;
        }else{
            return (double)minHeapHead;
        }
    }

    public void modifyTwoHeapSize(){
        if(minHeap.size() == maxHeap.size() + 2){
            maxHeap.offer(minHeap.poll());
        }
        if(maxHeap.size() == minHeap.size() + 2){
            minHeap.offer(maxHeap.poll());
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.Insert(1);
        solution.Insert(0);
        System.out.println(solution.GetMedian());
    }
}
