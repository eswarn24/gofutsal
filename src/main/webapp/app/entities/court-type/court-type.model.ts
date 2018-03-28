import { BaseEntity } from './../../shared';

export class CourtType implements BaseEntity {
    constructor(
        public id?: number,
        public type?: string,
    ) {
    }
}
