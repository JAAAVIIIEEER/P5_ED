package ule.edi.tree;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;





public class BinarySearchTreeTests {

   
	/*
	* 10
	* |  5
	* |  |  2
	* |  |  |	∅
	* |  |  |	∅
	* |  |	 ∅
	* |  20
	* |  |  15
	* |  |  |	∅
	* |  |  | 	∅
	* |  |  30
	* |  |  |  	∅
	* |  |  |  	∅
    */	
	private BinarySearchTreeImpl<Integer> ejemplo = null;
	
	
	/*
	* 10
	* |  5
	* |  |  2
	* |  |  |  	∅
	* |  |  |  	∅
	* |  | 	 ∅
	* |  20
	* |  |  15
	* |  |  |  12
	* |  |  |  |  	∅
	* |  |  |  |  	∅
	* |  | 	 ∅
  */
	private BinarySearchTreeImpl<Integer> other=null;
	
	@Before
	public void setupBSTs() {
		
			
		ejemplo = new BinarySearchTreeImpl<Integer>();
		ejemplo.insert(10, 20, 5, 2, 15, 30);
		Assert.assertEquals(ejemplo.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}");
		
		
		other =new BinarySearchTreeImpl<Integer>();
		other.insert(10, 20, 5, 2, 15, 12);
		Assert.assertEquals(other.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}}");
		
	    	}
	
	@Test
	public void testRemoveCountMayor1() {
		ejemplo.insert(20);
		ejemplo.insert(20);
		Assert.assertEquals(ejemplo.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20(3), {15, ∅, ∅}, {30, ∅, ∅}}}");
		ejemplo.remove(20);
	    Assert.assertEquals(ejemplo.toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20(2), {15, ∅, ∅}, {30, ∅, ∅}}}");
	}
	
	@Test
	public void testRemoveCountMayor1HastaVaciar() {
		ejemplo.insert(20);
		ejemplo.insert(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20(3), {15, ∅, ∅}, {30, ∅, ∅}}}",ejemplo.toString());
		ejemplo.remove(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20(2), {15, ∅, ∅}, {30, ∅, ∅}}}",ejemplo.toString());
		ejemplo.remove(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}",ejemplo.toString());
		ejemplo.remove(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {30, {15, ∅, ∅}, ∅}}",ejemplo.toString());
	}
	
	@Test
	public void testRemoveHoja() {
		ejemplo.remove(30);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, ∅}}",ejemplo.toString());
	}
	
	@Test
	public void testRemove1Hijo() {
		ejemplo.remove(5);
		Assert.assertEquals("{10, {2, ∅, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}",ejemplo.toString());
	}
	
	@Test
	public void testRemove2Hijos() {
		ejemplo.remove(10);
		Assert.assertEquals("{15, {5, {2, ∅, ∅}, ∅}, {20, ∅, {30, ∅, ∅}}}",ejemplo.toString());
	}
	
		
	@Test
	public void testTagHeightLeafEjemplo() {
			other.tagHeightLeaf();
			other.filterTags("height");
			Assert.assertEquals("{10, {5, {2 [(height, 3)],"+" ∅, ∅}, ∅}, {20, {15, {12 [(height, 4)],"+" ∅, ∅}, ∅}, ∅}}",other.toString());
		}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertException() {
		Integer i = null;
		other.insert(i);	
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testContainsNull() {
		other.contains(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveNullElement() {
		Integer i = null;
		other.remove(i);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testRemoveNoSuchElement() {
		other.remove(11);
	}			
		
	@Test
	public void testInsert() {
		ejemplo.insert(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20(2), {15, ∅, ∅}, {30, ∅, ∅}}}", ejemplo.toString());
	}
	
	// getSubtreeWithPath
	
	@Test
	public void testGetSubtreeWithPath() {
		ejemplo.insert(20);
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20(2), {15, ∅, ∅}, {30, ∅, ∅}}}", ejemplo.toString());
		Assert.assertEquals("{2, ∅, ∅}", ejemplo.getSubtreeWithPath("LL").toString());
		Assert.assertEquals("{20(2), {15, ∅, ∅}, {30, ∅, ∅}}", ejemplo.getSubtreeWithPath("R").toString());		
		Assert.assertEquals("{10, {5, {2, ∅, ∅}, ∅}, {20(2), {15, ∅, ∅}, {30, ∅, ∅}}}", ejemplo.getSubtreeWithPath("").toString());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testGetSubtreeWithPathNoSuch() {
		ejemplo.getSubtreeWithPath("LLL");
	}
	
	// getPath
	
	@Test
	public void testGetPath() {
		Assert.assertEquals("L", ejemplo.getPath(5));
		Assert.assertEquals("LL", ejemplo.getPath(2));
		Assert.assertEquals("R", ejemplo.getPath(20));
		Assert.assertEquals("RR", ejemplo.getPath(30));
		Assert.assertEquals("RL", ejemplo.getPath(15));
		Assert.assertEquals("", ejemplo.getPath(10));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testGetPathNoSuch() {
		ejemplo.getPath(55);
	}
	
	// tagPosDescend 
	
	@Test
	public void testTagPosDescend() {
		ejemplo.tagPosDescend();
		Assert.assertEquals(ejemplo.toString(), "{10 [(descend, 4)], {5 [(descend, 5)], {2 [(descend, 6)], ∅, ∅}, ∅}, {20 [(descend, 2)], {15 [(descend, 3)], ∅, ∅}, {30 [(descend, 1)], ∅, ∅}}}");
	}

	// tagInternalInorder 
	
	@Test
	public void testTagInternalInorder() {
		Assert.assertEquals(3,ejemplo.tagInternalInorder());
		Assert.assertEquals(ejemplo.toString(), "{10 [(internal, 3)], {5 [(internal, 2)], {2, ∅, ∅}, ∅}, {20 [(internal, 5)], {15, ∅, ∅}, {30, ∅, ∅}}}");
	}
	
	// copy
	
	@Test
	public void testCopy() {
		Assert.assertEquals(ejemplo.copy().toString(), "{10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}");
	}
	
	// getRoadUpRight
	
	@Test
	public void testGetRoadUpRight() {
		Assert.assertEquals(30, ejemplo.getRoadUpRight(2, 2, 2).intValue());
		Assert.assertEquals(ejemplo.toString(), "{10 [(road, 3)], {5 [(road, 2)], {2 [(road, 1)], ∅, ∅}, ∅}, {20 [(road, 4)], {15, ∅, ∅}, {30 [(road, 5)], ∅, ∅}}}");
	}
	
	// tagOnlySonPreorder
	
	@Test
	public void testTagOnlySonPreorder() {
		BinarySearchTreeImpl<Integer> ejemplo2 = new BinarySearchTreeImpl<Integer>();
		ejemplo2.insert(30, 10, 5, 2, 20, 15, 12);
		Assert.assertEquals(ejemplo2.toString(), "{30, {10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}}, ∅}");
		Assert.assertEquals(4,ejemplo2.tagOnlySonPreorder());
		Assert.assertEquals(ejemplo2.toString(), "{30, {10 [(onlySon, 2)], {5, {2 [(onlySon, 4)], ∅, ∅}, ∅}, {20, {15 [(onlySon, 6)], {12 [(onlySon, 7)], ∅, ∅}, ∅}, ∅}}, ∅}");
	}
}


