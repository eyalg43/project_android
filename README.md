# VidTube-Android app
<img src="/Android-app-screenshots/logo_vidtube.png" alt="Example Image" width="300">

NOTE: this branch is for part 1 of the project, for the frontend that is adjusted to work with the server (part 3 of the project), go to branch "mainPart3".

The VidTube app is a video sharing app, made for allowing users across the world to upload and share their video content, comment their thoughts and interact with each other.
This Readme file contains an overview of the Android app and its features, along with a guide on how to run it on your machine.

## How To Run
In order to run the Android app, start by cloning the repository to Android Studio, you can do this by opening the terminal and entering this command:
`git clone https://github.com/eyalg43/project_android.git`

After cloning the repository, open the project in Android Studio and run the app on an emulator or a physical device.

## Our work process:
We started off the same way we started the Web project, we watched Hemi's videos on the moodle tם help us build a basis of knowledge for the project, and then we continued on our own. Each of us had a responsibility for a different part of the project, Omri's part was the sign-in, sign-up and upload video pages, Max's part was the home page and Eyal's responsibility was the video page. Each of us finished his part and we connected the pages together, this was a bit harder than the Web project but we managed to figure it out. After finishing it we tested the logic of the app and fixed bugs until there were no more issues. While working on the app we used Jira to keep track of our tasks and it helped us keep our work organized.

## Android App Features:
* After running the app, the emulator (or device if ran on a physical device) will open and you will be brought to the **Home Activity**

* The **Home Activity** contains many exciting features such as:
    * A video list displaying some of the videos on the platform
    * A search bar where you can filter videos by title
    * A side menu where you have the ability to sign in (or sign up and then sign in) which unlocks more features such as:
        * Upload video - share your videos and content with the world (note: videos must contain a title, description, thumbnail etc.).
        * Edit video - allows you to change video title, description, thumbnail, or the video itself.
        * Delete video - don't want your video to appear on VidTube? you can easily delete it.
        * Commenting on a video (in the watch-video Activity).
    * Dark mode, which works across the entire app, this can be accessed and toggled via the side menu.

* The **Watch-Video** Activity contains even more great functionality:
    * The video itself with Pause, Play and a time line to jump ahead or back.
    * Interesting details about the video such as the author, author image, views, how long ago the video was uploaded, the description etc.
    * A list under the video with lots of recommended videos for you to watch and enjoy, each better than the last.
    * The ability to share your thoughts with buttons such as 'Like', 'Dislike', and 'Subscribe'.
    * You can also share your thoughts via the comment section (note: you must be logged in to comment, and comment must contain text)
      where you can like or dislike other peoples' comments, post a comment on your behalf with your Display name and profile picture,
      and also delete and edit comments (again, you must be logged in to edit and delete).

* The **Upload-Video** Activity:
    * Here you can share your video content with the world!
    * Choose a catchy title, informative description, an eye catching thumbnail and then select the video you want to share.
      click 'Upload To Vidtube' and your video can be seen on VidTube!
    * Your new video will be posted under your Display name and your profile picture.
    * Having second thoughts? don't worry, just click the 'cancel' button and you will be brought back to the Home Activity!

* The **Edit-Video** Activity:
    * We believe in second chances! Here you can edit videos, and change their title, description, thumbnail or the video itself.
    * Having second thoughts? don't worry, just click the 'cancel' button and you will be brought back to the Home Activity!

* The **Sign-in** Activity:
    * Sign in with your Username and password (make sure your account exists first) to unlock features such as commenting, editing, deleting and uploading videos and many more!
    * Don't have an account? No worries! click the "Don't have an account? Sign up here." text and you will be brought to the Sign-up Activity, where you can create an account.

* The **Sign-up** Activity:
    * Sign up by choosing your own Username, Display name, profile picture and password (and Verifying your password) to create your very own VidTube account and unlock features such as commenting, editing, deleting and uploading videos and many more!
    * You can upload a profile picture from your phone, or take a picture using your phone camera using the "Take photo" button.
    * Make sure your password fits the criteria and that your password and verification password match, or else you wont be allowed to sign up.


# 📷 Screenshots from the Android app:
## Home Activity (and switching to dark mode):
<img src="/Android-app-screenshots/Screenshot_1.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_2.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_3.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_4.png" alt="Example Image">

## Log in Activity (and signing up):
<img src="/Android-app-screenshots/Screenshot_5.png" alt="Example Image">

## Sign-up Activity (and uploading image from camera):
<img src="/Android-app-screenshots/Screenshot_6.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_7.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_8.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_9.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_10.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_11.png" alt="Example Image">

## Watch-Video Activity:
<img src="/Android-app-screenshots/Screenshot_37.png" alt="Example Image">

## Watch-Video Activity (Like, Dislike, Comments-post, edit, delete, like, dislike):
<img src="/Android-app-screenshots/Screenshot_19.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_20.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_21.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_22.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_23.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_24.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_25.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_26.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_27.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_28.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_30.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_29.png" alt="Example Image">

## Watch-Video Activity - Dark Mode:
<img src="/Android-app-screenshots/Screenshot_18.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_36.png" alt="Example Image">

## Upload-Video Activity:
<img src="/Android-app-screenshots/Screenshot_12.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_40.jpg" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_41.jpg" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_15.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_16.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_17.png" alt="Example Image">

## Edit-Video Activity (and editing a video):
<img src="/Android-app-screenshots/Screenshot_31.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_32.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_33.png" alt="Example Image">
<img src="/Android-app-screenshots/Screenshot_34.png" alt="Example Image">

## Edit-Video Activity - Dark Mode:
<img src="/Android-app-screenshots/Screenshot_35.png" alt="Example Image">
