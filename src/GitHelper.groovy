class GitHelper implements Serializable {
	
	private final Script script;
	
	GitHelper(Script script) {
		this.script = script
	}
	
	List getBranches(repoUrl){
		script.echo("fetching brancesh from url " + repoUrl)
		
		remotes = script.bat(returnStdout: true, script: '@echo off | git branch -a | findstr \"remotes/origin\"')
		remotes = remotes.replace("remotes/origin/", "")
	
		List result =  [] 
	
		remotes.readLines().each {line ->
			result.add(line.trim())
		}
	
		return result
	}
}