[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/mauriciotogneri/androidutils/blob/master/LICENSE.md)
[![Download](https://api.bintray.com/packages/mauriciotogneri/maven/androidutils/images/download.svg)](https://bintray.com/mauriciotogneri/maven/androidutils/_latestVersion)

# Android Utils
A collection of utility classes for Android.

## Installation
Add the following dependency to your module `build.gradle` file:

```groovy
implementation 'com.mauriciotogneri:androidutils:4.14.0'
```

And the following dependency to your app `build.gradle` file:

```groovy
allprojects
{
    repositories
    {
        maven
        {
            url 'https://s3.amazonaws.com/repo.commonsware.com'
        }
    }
}
```