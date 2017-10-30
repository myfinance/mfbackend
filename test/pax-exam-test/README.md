

Running any of these PaxExam Tests actually starts two distinct processes. One for the Unit Test class doing all the Setup, and one for each Unit Test Method. PaxExam allows to start these extra VMs with extra parameters. Which in turn allows us to enable remote debugging

This way the actual Unit Test needs to be started with an extra -D option (-DPAXDEBUG) and every spawned Unit test Method is suspended until the Remote Debugger is connected to it.

Pax-Exam is the only/best Framework I've found to write Tests with OSGI-Context. Unfortunately it causes problems in Build-Server environments. The Processes will not stop properly in cse of errors ...
So I decided to use it only locally and skipped it otherwise due to profiles.

