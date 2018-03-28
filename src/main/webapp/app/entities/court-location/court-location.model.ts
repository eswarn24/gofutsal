import { BaseEntity } from './../../shared';

export class CourtLocation implements BaseEntity {
    constructor(
        public id?: number,
        public region?: string,
        public address?: string,
        public state?: string,
        public country?: string,
    ) {
    }
}
