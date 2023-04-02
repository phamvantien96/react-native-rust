import { ServerCallContext, ServiceInfo } from '@protobuf-ts/runtime-rpc';
import {NativeEventEmitter, NativeModules} from 'react-native';
import { Empty } from './generated/google/protobuf/empty';
import { AddRequest, AddResponse, MathService as MathServiceType } from './generated/math';
import { IMathService } from './generated/math.server';

const {RNChannel} = NativeModules;
const channel = new NativeEventEmitter(RNChannel);

interface ChannelData {
	uuid: string,
	request: string,
}

export const startChannel = () => {
    const server = new RNServer();
    const mathService = new MathService();
    server.addService(MathServiceType, mathService);
}

class MathService implements IMathService {
    async sub(request: Empty, context: ServerCallContext): Promise<Empty> {
        console.log("MainActivity", "sub");
        return Empty;
    }

    async add(request: AddRequest, context: ServerCallContext): Promise<AddResponse> {
        console.log("MainActivity", request);
        return {result: request.firstNumber + request.secondNumber};
    }
}

class RNServer {
    addService(si: ServiceInfo, impl: any) {
        for (let mi of si.methods) {
			const implFn = impl[mi.localName].bind(impl);
			const eventName = si.typeName + "." + mi.localName;

			channel.addListener(eventName.toLowerCase(), async (data: ChannelData) => {
                const req = mi.I.fromJsonString(data.request)
				const res = await implFn(req);
                RNChannel.response(data.uuid, mi.O.toJsonString(res));
			})
        }
    }
}
