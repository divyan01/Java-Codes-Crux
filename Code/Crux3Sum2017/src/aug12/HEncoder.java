package aug12;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;

import july31.GenericHeap;

public class HEncoder {
	private HashMap<Character, String> encoder = new HashMap<>();
	private HashMap<String, Character> decoder = new HashMap<>();
	
	private static class Node{
		Character data;
		int freq;
		Node left;
		Node right;
		private static final NodeComparator Ctor = new NodeComparator();
		
		private static class NodeComparator implements Comparator<Node>{
			@Override
			public int compare(Node o1, Node o2) {
				// TODO Auto-generated method stub
				return o2.freq - o1.freq;
			}
		}
	}
	
	// 1. freq map
	// 2. prepare the heap from keyset
	// 3. prepare tree - remove two, merge, add it back
	// 4. traverse
	public HEncoder(String feeder){
		// 1. freq map
		HashMap<Character, Integer> fm = new HashMap<>();
		for(int i = 0; i < feeder.length(); i++){
			char ch = feeder.charAt(i);
			
			if(fm.containsKey(ch)){
				fm.put(ch, fm.get(ch) + 1);
			} else {
				fm.put(ch, 1);
			}
		}
		
		// 2. create the heap
		GenericHeap<Node> heap = new GenericHeap<>(Node.Ctor);
		ArrayList<Character> keys = new ArrayList<>(fm.keySet());
		for(Character key: keys){
			Node node = new Node();
			node.data = key;
			node.freq = fm.get(key);
			
			heap.add(node);
		}
		
		// 3. create the binary tree - remove two, merge, put it back till size is 1
		while(heap.size() != 1){
			Node one = heap.removeHP();
			Node two = heap.removeHP();
			
			Node merged = new Node();
			merged.freq = one.freq + two.freq;
			merged.left = one;
			merged.right = two;
			
			heap.add(merged);
		}
		
		// 4. traverse the tree
		Node finalNode = heap.removeHP();
		traverse(finalNode, "");
	}

	private void traverse(Node node, String osf) {
		// work
		if(node.left == null && node.right == null){
			encoder.put(node.data, osf);
			decoder.put(osf, node.data);
			return;
		}
		
		traverse(node.left, osf + "0");
		traverse(node.right, osf + "1");
	}

	public String compress(String str) throws Exception {
		String rv = "";
		
		for(int i = 0; i < str.length(); i++){
			rv += encoder.get(str.charAt(i));
		}
		
		byte[] barr = null;
		
		if(rv.length() % 8 == 0){
			barr = new byte[rv.length() / 8];
		} else {
			barr = new byte[rv.length() / 8 + 1];
		}
		
		int counter = 0;
		for(int i = 0; i < rv.length(); ){
			barr[counter] = (byte)Integer.parseInt(rv.substring(i, Math.min(i + 8, rv.length())), 2);
			counter++;
			i = i + 8;
		}
		
		Path path = Paths.get("E:\\1.obj");
		Files.write(path, barr);
		
		return rv;
	}
	
	public String decompress(String str) throws Exception{
		Path path = Paths.get("E:\\1.obj");
	    byte[] arr = Files.readAllBytes(path);
	    
	    for(int i = 0; i < arr.length; i++){
	    	str += String.format("%8s", Integer.toBinaryString(arr[i] & 0xFF)).replace(' ', '0');
	    }
	    
		String rv = "";
		
		String code = "";
		for(int i = 0; i < str.length(); i++){
			code += str.charAt(i);
			
			if(decoder.containsKey(code)){
				rv += decoder.get(code);
				code = "";
			}
		}
		
		return rv;
	}
}
