package newandshinythings;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Timestamp {
	@SuppressWarnings("unused")
	@PrimaryKey
	@Persistent
	private Long primaryKey;
	
	@Persistent
	private long millis;
	
	public Timestamp(long primaryKey, long millis){
		this.primaryKey = primaryKey;
		this.millis = millis;
	}
	
	public long getMillis(){ return this.millis;}

	public void setMillis(long millis) {
		this.millis = millis;
	}
}
