import { BaseEntity } from './../../shared';

export const enum UserBookingStatus {
    'Requested',
    'Apporved'
}

export class Booking implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public startTime?: any,
        public endTime?: any,
        public status?: UserBookingStatus,
        public court?: BaseEntity,
    ) {
    }
}
