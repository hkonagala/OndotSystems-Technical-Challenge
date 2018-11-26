# OndotSystems-Technical-Challenge

An attempt at parsing images from Pixabay API using volley library and ViewModel architecture in android. The project contains two screens, first asks for fingerprint authentication, on successful authentication, navigates to another screen containing a edittext for search, button and recyclerview which shows results from Pixabay API.

The project uses the following libraries: FingerAuth for fingerprint validation, volley for networking (easy & automatic scheduling of network request, caching etc), Picasso for image processing, lottie for animations, gson for serializing/de-serializing java objects

The project also handles edge cases like: no results from API, network errors, etc

How to run: Clone or download the repository on to the local machine. open in Android Studio and run it.

TODOs:

1.better handling of orientation changes using ViewModel class
2.applying better design pattern to the project (MVVM) via data binding, to separate data, logic and presentation

Other improvements: (beyond the scope of the assignment)

1.UI
2.pagination
3.alternate password/pin for fingerprint fail cases
