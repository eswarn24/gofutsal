import { BaseEntity } from './../../shared';

export class Booking implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public startTime?: any,
        public endTime?: any,
        public court?: BaseEntity,
        public bookingStatus?: BaseEntity,
    ) {
    }
}
