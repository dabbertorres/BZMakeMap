# BZMakeMap
GUI wrapper of the command line tools makeTRN and the Battlezone editor.

A version was originally written years ago in C++ and using OpenFrameworks. Yeah. A bit overkill. I was still a newbie to programming.

Anyway, I thought I had lost the source code, but I found it recently. Lets just say my code was terrifying. 
So, I decided to rewrite it, and since I needed some more Java on my Github, I decided to write it in Java. 

And this is the result!

Currently, it has about 80% feature parity as the original version. Maybe 50% if you include "prettiness".
I'll bring it up to 100% and then we'll see if I decide to add more features from there.

# What
Simple GUI wrapper around using and launching makeTRN and the BZ editor.
Uses the Windows Registry to find the BZ install.

##### Bonus new features over old version:
Custom planet files should be supported and selectable.

Tooltips for the options to help explain what they're for.

##### Notes:
Add to Netmis options does not work at the moment since I got tired and decided to finish up.

# Dependencies
Uses [JNA](https://github.com/twall/jna) for Windows Registry reading.

# License
[MIT](http://choosealicense.com/licenses/mit/)
