import { requireNativeModule } from 'expo'
import type { Spec } from './specs/NativeMdocDataTransfer'

export const requireExpoModule = () => requireNativeModule<Spec>('MdocDataTransfer')
