import { EventEmitter as ExpoEventEmitter } from 'expo-modules-core'
import { type NativeModule, NativeModules, Platform, NativeEventEmitter as ReactNativeEventEmitter } from 'react-native'
import { requireExpoModule } from './NativeMdocDataTransfer'
import type { OnErrorPayload, OnRequestReceivedEventPayload, OnResponseSendPayload } from './MdocDataTransferEvent'

import type { Spec } from './specs/NativeMdocDataTransfer'

type EventMap = {
  onRequestReceived: (payload: OnRequestReceivedEventPayload) => void
  onResponseSent: (payload: OnResponseSendPayload) => void
  onError: (payload: OnErrorPayload) => void
}

const shouldUseExpo = Platform.OS === 'android'

export const mDocNativeModule = shouldUseExpo ? requireExpoModule() : (NativeModules.MdocDataTransfer as Spec)

export const mDocNativeModuleEventEmitter = shouldUseExpo
  ? new ExpoEventEmitter<EventMap>(mDocNativeModule as any)
  : new ReactNativeEventEmitter(mDocNativeModule as unknown as NativeModule)
