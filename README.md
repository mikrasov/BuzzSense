BuzzSense
=========

As ecological observation are increasingly augmented by autonomous systems, the analysis and transmission 
of data poses serious problems for information manager. The aim of this project is to bridge the technological gap between 
the needs of biologists and recent advances in mobile imaging. 

To solve this we present BuzzSense, an application for Bee population counting running on an Android mobile device.

<h3> To run this application </h3>
An android device running Android 4.2 or better.

You will need the <A href="https://play.google.com/store/apps/details?id=org.opencv.engine">OpenCV Manager</a> 
available on the Play Store.

You will also need to copy images from the project <strong>./samples/</strong> directory
to <strong>/media/samples/</strong> on your mobile device.

<h3> To compile this application </h3>
You will need to download the <a href="http://opencv.org/">Open CV Android 2.4.8</a> or better SDK.

You will need to download and install the <a href="https://developer.android.com/tools/sdk/ndk/index.html">Android NDK (Native Development Kit)</a>.

This program uses native C++ libraries (wraped in easy to use Java calls) for hardware accelerated image processing.