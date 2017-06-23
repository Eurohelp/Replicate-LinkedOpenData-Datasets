package donosti.lod.replicate.eurohelp.es;

public enum LOD_DONOSTI_URI {
	base("http://lod.eruohelp.es/"),
	id(LOD_DONOSTI_URI.base.getUri() + "id/"),
	parking(LOD_DONOSTI_URI.id.getUri() + "parking/");
	
	private String uri;

	private LOD_DONOSTI_URI(String uri) {
		this.uri = uri;
	}
	public final String getUri() {
		return uri;
	}
}
