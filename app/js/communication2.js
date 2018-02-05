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
       <TouchableOpacity style={styles.container} onPress = {this.onPress.bind(this)}>
          <View style={{
          width:Dimensions.get('window').width,
          height:50,
          backgroundColor:'#dfd',
          alignItems:'center',
          justifyContent:'center'
          }}>
            <Text style={styles.text}>這是一個React Native按鈕,點擊調用原生Toast方法</Text>
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