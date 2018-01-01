public class Command{
	public String key;
	public String usage;
	public Event event;

	public Command(String key,String usage,Event event){
		this.key=key;
		this.usage=usage;
		this.event=event;
	}
}