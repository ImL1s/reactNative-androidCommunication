# React Native與Android交互


## Activity引用React Native Component

### 步驟
1.	新建一個Android專案
2. 在Android專案根目錄下執行以下指令,提示大部分按enter就好了
	
	> npm init
	
3. 繼續執行

	> npm install react react-native --save
	
4. 這時Android的目錄會是以下的樣子
	
		-YourProjectName
		|-.gradle
		|-.idea
		|-app
		|-build
		|-gradle
		|-node_modules
		|-.gitignore
		|-YourProjectName.iml
		|-build.gradle
		|-gradle.properties
		|-gradlew
		|-gradlew.bat
		|-local.properties
		|-npm-debug.log
		|-package.json
		|-settings.gradle
		|-yarn.lock

5. package.json的內容應該為下,react和react-native版本會隨著時間推移改變

		{
		  "name": "YourProjectName",
		  "version": "1.0.0",
		  "description": "",
		  "main": "index.js",
		  "scripts": {
		    "test": "echo \"Error: no test specified\" && exit 1"
		  },
		  "author": "",
		  "license": "ISC",
		  "dependencies": {
		    "react": "^16.2.0",
		    "react-native": "^0.52.2"
		  }
		}

6. 接著開啟Android專案的root gradle,將剛剛npm下載的react native
Android library放到repositories中

		allprojects {
	    repositories {
	        jcenter()
	        maven { url 'https://maven.google.com' }
	        // 將node_modules中Android相關的lib放到maven引用中
	        maven {
	            // All of React Native (JS, Android binaries) is installed from npm
	            url "$rootDir/node_modules/react-native/android"
	        }
	    }
	}
	
7. 接著到app的gradle中,將react native相關的lib引入

		dependencies {
		 ... ...
	   	compile "com.facebook.react:react-native:+" // From node_modules.
	}
	
8. 配置完成,打開MainActivity,在onCraete中
	
		 //檢查權限：讓使用者打開懸浮視窗權限以便開發中的紅屏錯誤能正確顯示
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
	            if (!Settings.canDrawOverlays(this)) {
	                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
	                        Uri.parse("package:" + getPackageName()));
	                startActivityForResult(intent, 154);
	            }
	        }
	        
8. Android端的作業做得差不多了,現在來寫一下react native的部分吧,在Android專案根目錄新增一個目錄js,在裡面新增一個index.js,這個就是等等Activity要用來顯示的react native component

		import React from 'react';
		import {
		  AppRegistry,
		  StyleSheet,
		  Text,
		  View
		} from 'react-native';
		
		class HelloWorld extends React.Component {
		  render() {
		    return (
		      <View style={styles.container}>
		        <Text style={styles.text}>I'm React Native Text</Text>
		      </View>
		    )
		  }
		}
		var styles = StyleSheet.create({
		  container: {
		    flex: 1,
		    justifyContent: 'center',
		  },
		  text: {
		    fontSize: 20,
		    textAlign: 'center',
		    margin: 10,
		  },
		});
		
		AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);
		
9. 接下來在Activity中

		class MainActivity extends AppCompatActivity {
	
	    private ReactRootView mReactRootView;
	    private ReactInstanceManager mReactInstanceManager;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	
	        mReactRootView = new ReactRootView(this);
	        mReactInstanceManager = ReactInstanceManager.builder()
	                .setApplication(getApplication())
	                // 在asset文件夾中,打包過的react native js文件名稱
	                .setBundleAssetName("index.android.bundle")
	                .addPackage(new MainReactPackage())
	                .setUseDeveloperSupport(BuildConfig.DEBUG)
	                .setInitialLifecycleState(LifecycleState.RESUMED)
	                .build();
	
	        // 第二個參數"MyReactNativeApp"為React Native中的AppRegistry.registerComponent的第一個參數
	        mReactRootView.startReactApplication(mReactInstanceManager, "MyReactNativeApp", null);
	
	        setContentView(mReactRootView);
	    }
	}
	
10. 將剛剛寫好的react native component打包成bundle,給ReactInstanceManager使用,bundle會放在Android的asset下

		react-native bundle --platform {{平台}} --entry-file {{入口文件,一般命名index.js}} --bundle-output {{打包好的bundle存放路徑}} --assets-dest {{react native引用的資源文件置放路徑}}
		
		react-native bundle --platform android --entry-file ./app/js/index.js --bundle-output app/src/main/assets/index.android.bundle --assets-dest app/src/main/res/

11.  恭喜!執行App,看到React Native的介面


## Activity 呼叫 React Native Component
TODO


## React Native Component 呼叫 Activity 
TODO

## 將現有的Android專案整React Native