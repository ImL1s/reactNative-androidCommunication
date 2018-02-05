# React Native與Android交互
- author: ImL1s
- email: iml1s@outlook.com

## Android引用React Native Component

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


## Android 呼叫 React Native Component

1. 新增一個implement ReactContextBaseJavaModule的class,這個class是最終與react native通信的類

		class ToastAndroidModule(private val reactContext: ReactApplicationContext) :
	        ReactContextBaseJavaModule(reactContext) {
	
		    /**
		     * react native call android時的模組名稱
		     */
		    override fun getName(): String {
		        return "ToastAndroidModule"
		    }
		
		    /**
		     * react native call(->) android
		     */
		    @ReactMethod // 此註解代表要expose給react native的方法
		    fun HandleMessage(message: String) {
		        Toast.makeText(reactContext, message, Toast.LENGTH_LONG).show()
		    }
		
		    /**
		     * android call(->) react native
		     */
		    fun sendMessage(params: String) {
		        reactContext
		                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
		                .emit("mEventName", params)
		    }
		}

	
2. 新增一個implement ReactPackage的Package,此類底下會有許多NativeModule(就是第一步驟寫的),必須將此類放入react native API,才能進行溝通

		class AndroidWidgetPackage : ReactPackage {

		    private var nativeModules: MutableList<NativeModule>? = null
		
		    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
		        // 在這裡將需要溝通原生模組放入list並回傳,這樣就等於將原生註冊到react native
		        nativeModules = ArrayList()
		        nativeModules!!.add(0, ToastAndroidModule(reactContext))
		        return nativeModules as ArrayList<NativeModule>
		    }
		
		    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
		        return Collections.emptyList()
		    }
		
		    /**
		     * 方便取得AndroidModule的方法
		     */
		    fun getModule(index: Int): NativeModule? {
		        return if (nativeModules == null) null else nativeModules!![index]
		    }
		
		    fun getToastModule(): ToastAndroidModule {
		        return getModule(0) as ToastAndroidModule
		    }
		}

3. 撰寫React Native的Component,此Component功能很簡單,點擊按鈕呼叫第一部撰寫的ToastAndroidModule中的HandleMessage,然後就會看到Toast出現

		 import React from 'react';
		 import {
		   AppRegistry,
		   StyleSheet,
		   Text,
		   View,
		   TouchableOpacity,
		   Dimensions,
		   NativeModules,
		   ToastAndroid,
		   DeviceEventEmitter
		 } from 'react-native';
		
		export default class Communication3 extends React.Component {
		
		      constructor(){
		        super();
		        this.state = {
		            info : "我是React Native寫的內容"
		        }
		      }
		
		    componentWillMount(){
		      DeviceEventEmitter.addListener('mEventName',
		                           this.rnMethod.bind(this));
		    }
		
		    rnMethod(params){
		      this.setState({info:params});
		    }
		
		   render() {
		     return (
		       <TouchableOpacity style={styles.container}>
		          <View style={{width:Dimensions.get('window').width,height:50,margin:10,
		              backgroundColor:'#dfd',alignItems:'center',justifyContent:'center'}}>
		                <Text style={styles.hello}>{this.state.info}</Text>
		          </View>
		       </TouchableOpacity>
		     )
		   }
		 }
		 var styles = StyleSheet.create({
		   container: {
		     flex: 1,
		     justifyContent: 'center',
		   }
		});
		
4. 將剛剛寫好的react native component打包成bundle,給ReactInstanceManager使用,bundle會放在Android的asset下

		react-native bundle --platform {{平台}} --entry-file {{入口文件,一般命名index.js}} --bundle-output {{打包好的bundle存放路徑}} --assets-dest {{react native引用的資源文件置放路徑}}
		
		react-native bundle --platform android --entry-file ./app/js/index.js --bundle-output app/src/main/assets/index.android.bundle --assets-dest app/src/main/res/
		
5. 接下來撰寫Activity的View
		
		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    android:id="@+id/root_view"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    android:orientation="vertical">
		    <Button
		        android:id="@+id/native_btn"
		        android:layout_width="match_parent"
		        android:layout_height="40dp"
		        android:layout_margin="10dp"
		        android:background="#ddf"
		        android:text="我是原生按鈕點擊我調用React Native方法" />
		        
		    <com.facebook.react.ReactRootView
		        android:layout_weight="1"
		        android:id="@+id/react_root_view1"
		        android:layout_width="match_parent"
		        android:layout_height="wrap_content" />
		        
		</LinearLayout>
		
6. 最後就是要將ReactPackage註冊到React Native API中了

		class ReactCommunicationActivity : AppCompatActivity() {
		    
		    private var mReactRootView: ReactRootView? = null
		    private var mReactInstanceManager: ReactInstanceManager? = null
		    private var reactPackage: AndroidWidgetPackage? = null
		    private var mClickTime = 0
		
		    override fun onCreate(savedInstanceState: Bundle?) {
		        super.onCreate(savedInstanceState)
		        setContentView(R.layout.activity_react_comunucatuin)
		        
		        mReactRootView = ReactRootView(this)
		        reactPackage = AndroidWidgetPackage()
		        mReactInstanceManager = ReactInstanceManager.builder()
		                .setApplication(application)
		                .setBundleAssetName("index.android.bundle")
		                .setJSMainModulePath("index")
		                .addPackage(MainReactPackage())
		                .addPackage(reactPackage)   //加入AndroidModule
		                .setUseDeveloperSupport(BuildConfig.DEBUG)
		                .setInitialLifecycleState(LifecycleState.RESUMED)
		                .build()
		
		        // 注意這裡的MyReactNativeApp必須對應“index.android.js”中的
		        // “AppRegistry.registerComponent()”的第一個參數
		        react_root_view1.startReactApplication(mReactInstanceManager, "Communication2", null)
		
		
		        //添加本地按鈕的點擊事件
		        native_btn.setOnClickListener {
		            reactPackage!!.getToastModule().sendMessage("這是一條Android發送給React的消息${mClickTime++}")
		        }
		    }
		}

7. 點擊按鈕,成功調用原生的Toast方法




## React Native Component 呼叫 Activity 

1. 在上一部的基礎上,在View加上

		<?xml version="1.0" encoding="utf-8"?>
		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    android:id="@+id/root_view"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    android:orientation="vertical">
	
	    <Button
	        android:id="@+id/native_btn"
	        android:layout_width="match_parent"
	        android:layout_height="40dp"
	        android:layout_margin="10dp"
	        android:background="#ddf"
	        android:text="我是原生按鈕點擊我調用React Native方法" />
	
	
	    <com.facebook.react.ReactRootView
	        android:layout_weight="1"
	        android:id="@+id/react_root_view1"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content" />
	
	    <!--新增一個react view-->
	    <com.facebook.react.ReactRootView
	        android:layout_weight="1"
	        android:id="@+id/react_root_view2"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content" />
	
		</LinearLayout>
		
2. 新增一個Component,此Componenet目的為顯示Android端點擊後,反應點擊

		import React from 'react';
		import {
		   AppRegistry,
		   StyleSheet,
		   Text,
		   View,
		   TouchableOpacity,
		   Dimensions,
		   NativeModules,
		   ToastAndroid
		} from 'react-native';
		
		export default class Communication extends React.Component {
		
		  onPress = ()=> {
		      // 這樣調用原生端方法,show出吐司
		      NativeModules.ToastAndroidModule
		      .HandleMessage("React Native 呼叫Native来吐司！！");
		  }
		
		   render() {
		     return (
		       <TouchableOpacity style={styles.container} 
		       onPress = {this.onPress.bind(this)}>
		          <View style={{
		          width:Dimensions.get('window').width,
		          height:50,
		          backgroundColor:'#dfd',
		          alignItems:'center',
		          justifyContent:'center'
		          }}>
		            <Text style={styles.text}>
		            這是一個React Native按鈕,點擊調用原生Toast方法
		            </Text>
		          </View>
		       </TouchableOpacity>
		     )
		   }
		 }
		 var styles = StyleSheet.create({
		   container: {
		     flex: 1,
		     justifyContent: 'center',
		   },
		   text:{
		       fontSize: 20
		   }
		});

3. 打包js

		# react-native bundle --platform {{平台}} --entry-file {{入口文件,一般命名index.js}} --bundle-output {{打包好的bundle存放路徑}} --assets-dest {{react native引用的資源文件置放路徑}}
		
		react-native bundle --platform android --entry-file ./app/js/index.js --bundle-output app/src/main/assets/index.android.bundle --assets-dest app/src/main/res/

4. 在Activity中,啟動該React Native Component的生命週期

		class ReactCommunicationActivity : AppCompatActivity() {	
		    private var mReactRootView: ReactRootView? = null
		    private var mReactInstanceManager: ReactInstanceManager? = null
		    private var reactPackage: AndroidWidgetPackage? = null
		    private var mClickTime = 0
		
		    override fun onCreate(savedInstanceState: Bundle?) {
		        super.onCreate(savedInstanceState)
		        setContentView(R.layout.activity_react_comunucatuin)
		
		        mReactRootView = ReactRootView(this)
		        reactPackage = AndroidWidgetPackage()
		        mReactInstanceManager = ReactInstanceManager.builder()
		                .setApplication(application)
		                .setBundleAssetName("index.android.bundle")
		                .setJSMainModulePath("index")
		                .addPackage(MainReactPackage())
		                .addPackage(reactPackage)   //加入AndroidModule
		                .setUseDeveloperSupport(BuildConfig.DEBUG)
		                .setInitialLifecycleState(LifecycleState.RESUMED)
		                .build()
		
		        // 注意這裡的MyReactNativeApp必須對應“index.android.js”中的
		        // “AppRegistry.registerComponent()”的第一個參數
		//        mReactRootView!!.startReactApplication(mReactInstanceManager, "Communication3", null)
		        react_root_view1.startReactApplication(mReactInstanceManager, "Communication2", null)
		        react_root_view2.startReactApplication(mReactInstanceManager, "Communication3", null)
		
		
		        //添加本地按鈕的點擊事件
		        native_btn.setOnClickListener {
		            reactPackage!!.getToastModule().sendMessage("這是一條Android發送給React的消息${mClickTime++}")
		        }
	    	}
		}

5. 測試,完工!



## 將現有的Android專案整React Native

// TODO