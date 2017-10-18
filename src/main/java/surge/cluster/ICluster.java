package surge.cluster;

public interface ICluster<T>
{
	public ClusterType getType();

	public void set(T t);

	public T get();
}
