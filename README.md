# Image_Loader
 
 Objective of this app is to build a basic library for performing following functions:
 
 -Asynchronous Image loading
 -Memory Level caching
 -Disk Level caching.
 
 To attain above features following libraries is used:
-Retrofit2
-OkHttp3
-JakeWharton DiskLRUCache
-RecyclerView
-ConstraintLayout


Application Flow

- Application queries list of photos from www.unsplash.com
- Responses are in form json where url of images are give.
- HTTP GET connection for  those images in form of bytestream
- Response from above HTTP requests are displayed in recyclerview.
- Images are selected are opened in full screen using Fragments.


Code Structure.
-disklrucache
    Boilerplatecode for  implementation of DiskLruCache.
-loader -->This is library which is made for custom request.
    |------CacheRequest --> This class is for caching bitmaps in memory level.
    |------DiskLruCacheRequest --> This class is for caching bitmaps in disk level.
    |------NetworkInstance --> This class is for querying images based on url.
    |------ImageLoaderApp --> This class is builder class for requests made from MainActivity
-models
    |------Photo, Urls --> Model class for response from www.unsplash.com.
-unplashloader --> This package is for requesting  from www.unsplash.com using retrofit2.
-utils
--ImageFragment --> Displaying image from list in full screen.
--MainActivity --> This is presenter class which binds model class and views.


ImageLoaderApp class is main class for querying bitmaps from URL. This builder class has api for MainActivity such as:

get(Context) -> For Initialization of helper classes. 
load(String url) -> To load from bitmaps from URL.
addMemCache() -> Call this function if Bitmap needs to be cached in memory.
addDiskCache() -> Call this function if Bitmap needs to be cached in disk.

# Deep Dive

Application uses Builder pattern, Singleton Pattern and Mediator pattern. 

To get requests from url application uses Retrofit2 library. Retrofit2 is faster then other HTTP libraries and provides easy implementation and readable response using POJO classes(Photo, Urls).

Here we have loaded for a reference of 25 images which are asynchronously. This can be scaled up to more then that. Since application is not aware how many images should be shown.

Fragments is used to display image in full screen to reduce and minimize the memory instead of using another activity.
This application supports minSdk API Level 23.

