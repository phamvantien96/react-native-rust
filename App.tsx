/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import React, { useEffect } from 'react';
import {
  SafeAreaView,
  Text,
  useColorScheme,
} from 'react-native';

import {
  Colors,
} from 'react-native/Libraries/NewAppScreen';
import { startChannel } from './src/NativeModule';

function App(): JSX.Element {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  useEffect(() => {
    console.log("RNChannel: startChannel");
    setTimeout(() => startChannel(), 6000)
  }, []);

  return (
    <SafeAreaView style={backgroundStyle}>
      <Text>Hello, World</Text>
    </SafeAreaView>
  );
}

export default App;
