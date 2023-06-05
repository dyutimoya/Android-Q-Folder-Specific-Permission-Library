
# Folder Specific Permissions for Android sdk using Q or above



## Implementation
### Step 1. Add the JitPack repository to your build file

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```

### Step 2. Add the dependency

```
dependencies {
	        implementation 'com.github.dyutimoya:Android-Q-Folder-Specific-Permission-Library:Tag'
	}

```
### Step 3. Call the constractor method and GetFolderPermission

```
GetFolderPermission permission = new GetFolderPermission(this);

if (permission.isPermissionAlreadyTaken("key")){
            System.out.println(Arrays.toString(permission.GetDocumentFiles("key")));
        } else {
            permission.TakePermission("/Download/Test"); // Your folder name for which you want the permission
        }

```

### Step 4. Get the files uri list at onActivityResult()

```
@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        DocumentFile[] documentFiles = permission.GetResult(data, "key");

    }

```


## Authors

- [@dyutimoya](https://github.com/dyutimoya)

