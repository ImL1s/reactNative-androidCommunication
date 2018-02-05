import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';
import Communication1 from './communication1.js';
import Communication2 from './communication2.js';
import Communication3 from './communication3.js';

class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.hello}>我是Rn 界面</Text>
      </View>
    )
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('MyReactNativeApp', () => HelloWorld);
AppRegistry.registerComponent('Communication1', () => Communication1);
AppRegistry.registerComponent('Communication2', () => Communication2);
AppRegistry.registerComponent('Communication3', () => Communication3);