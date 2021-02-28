# jenkins-mini-status

Generates html that uses/abuses the jenkins build status icons to display many job status at once.

![example](https://github.com/SysLord/jenkins-mini-status/blob/main/example_output.png "Example html output")

By using the build status icon from .../lastBuild/status we get the real time status (at html load time)

Html needs to re-generated when jobs change or jobs are disabled/enabled.

TODO: Make ready for jenkins use as a groovy library (using /var and /src)

TODO: Finding disabled jobs is not yet implemented.
