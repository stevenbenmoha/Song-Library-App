//Created by Steven Benmoha and Colin Ackerley
package view;
public class Song implements Comparable<Object>
{
	String name; //Fields that define a song
	String artist;
	String album;
	String year;
	public Song(String name, String artist) //Bare minimum information required
	{
		this.name = name;
		this.artist = artist;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getArtist()
	{
		return artist;
	}
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	public String getAlbum()
	{
		return album;
	}
	public void setAlbum(String album)
	{
		this.album = album;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public String toString()
	{
		return this.name + " - " + this.artist;
	}
	public int compareTo(Object o) //Compare a song to see if they're considered equivalent
	{
		String songA = this.name + this.artist;
		Song other = (Song) o;
		String songB = other.name + other.artist;
		return songA.compareToIgnoreCase(songB);
	}
}