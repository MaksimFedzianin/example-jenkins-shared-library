class GitHelper implements Serializable {
	
	private final Script script;
	
	GitHelper(Script script) {
		this.script = script
	}
	
	List getBranches(repoUrl){
		script.echo("fetching brancesh from url " + repoUrl)
		return ['one', 'two', 'three']
	}
}