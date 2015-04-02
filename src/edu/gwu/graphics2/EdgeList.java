package edu.gwu.graphics2;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EdgeList implements Iterable<Entry>{
	
	private final LinkedList<Entry> entries;
	
	public EdgeList()
	{
		entries = new LinkedList<Entry>();
	}
	
	
	public void add(Entry entry)
	{
		int index = Collections.binarySearch(entries, entry);
		if(index < 0)
		{
			index = -index-1;
		}
		entries.add(index, entry); //insert in order
	}
	
	public void addAll(EdgeList other)
	{
		for(Entry ent : other.entries)
		{
			int index = Collections.binarySearch(entries, ent);
			if(index < 0)
			{
				index = -index-1;
			}
			entries.add(index, ent);
		}
	}
	
	public List<Segment> getSegments()
	{
		List<Segment> segs = new LinkedList<Segment>();
		Iterator<Entry> itr = entries.iterator();
		while(itr.hasNext())
		{
			Entry first = itr.next();
			if(itr.hasNext())
			{
				Entry second = itr.next();
				segs.add(new Segment(first, second));
			}
		}
		return segs;
	}
	
	public void removeAll(Collection<Entry> torem)
	{
		entries.removeAll(torem);
	}
	
	public void sort()
	{
		Collections.sort(entries);
	}


	@Override
	public Iterator<Entry> iterator() {
		return entries.iterator();
	}

}
