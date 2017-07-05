

Running any of these PaxExam Tests actually starts two distinct processes. One for the Unit Test class doing all the Setup, and one for each Unit Test Method. PaxExam allows to start these extra VMs with extra parameters. Which in turn allows us to enable remote debugging

This way the actual Unit Test needs to be started with an extra -D option (-DPAXDEBUG) and every spawned Unit test Method is suspended until the Remote Debugger is connected to it.
