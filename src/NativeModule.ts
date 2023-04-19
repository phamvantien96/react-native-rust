import { createServiceImplSpec, MethodImpl, ServiceImpl, ServiceImplSpec, StreamResponse, Transport, UnaryResponse } from '@bufbuild/connect';
import { Message, AnyMessage, ServiceType, MethodInfo, PartialMessage } from '@bufbuild/protobuf';
import {NativeEventEmitter, NativeModules} from 'react-native';
import { AddRequest, AddResponse } from './generated/math_pb';
import { MathService } from './generated/math_connect'
const {RNChannel} = NativeModules;
const channel = new NativeEventEmitter(RNChannel);

class RNTransport implements Transport {
    unary<I extends Message<I> = AnyMessage, O extends Message<O> = AnyMessage>(service: ServiceType, method: MethodInfo<I, O>, signal: AbortSignal | undefined, timeoutMs: number | undefined, header: any, input: PartialMessage<I>): Promise<UnaryResponse<I, O>> {
        throw new Error('Method not implemented.');
    }
    stream<I extends Message<I> = AnyMessage, O extends Message<O> = AnyMessage>(service: ServiceType, method: MethodInfo<I, O>, signal: AbortSignal | undefined, timeoutMs: number | undefined, header: any, input: AsyncIterable<I>): Promise<StreamResponse<I, O>> {
        throw new Error('Method not implemented.');
    }
}

export const addMethod: MethodImpl<typeof MathService.methods.add> = async (request: AddRequest) => {
    return new AddResponse({result: 10});
}

export const mathService: ServiceImpl<typeof MathService> = {
    add: addMethod
};

interface ChannelData {
	uuid: string,
	request: string,
}

export const startChannel = () => {
    const server = new RNServer();
    server.addService(createServiceImplSpec(MathService, mathService));
}

class RNServer {
    addService(serviceSpec: ServiceImplSpec) {
        for (const [localName, methodInfo] of Object.entries(serviceSpec.methods)) {
            const eventName = serviceSpec.service.typeName + "." + localName;
        	channel.addListener(eventName.toLowerCase(), async (data: ChannelData) => {
                const req = methodInfo.method.I.fromJsonString(data.request);
				const res = await methodInfo.impl(req, undefined);
                RNChannel.response(data.uuid, JSON.stringify(res));
			})
        }
    }
}
